#!/usr/bin/env node
// prebuild: prints the build machine's resources at the start of `yarn build`, so a
// Netlify build log shows what the build had to work with: CPUs as Node
// sees them (os.availableParallelism(), which Docusaurus uses to size its
// SSG worker thread pool) and as the container's cgroup limits them (a CFS
// quota is invisible to Node), memory, and the Node version. Informational
// only: it never fails the build.
const fs = require('fs');
const os = require('os');

function read(file) {
  try { return fs.readFileSync(file, 'utf8').trim(); } catch { return null; }
}
const mb = (bytes) => `${Math.round(bytes / 1024 / 1024)} MB`;

try {
  const cpus = os.cpus();
  const parts = [
    `node ${process.version}`,
    `cpus ${cpus.length}${cpus[0] ? ` (${cpus[0].model.trim()})` : ''}`,
    `availableParallelism ${typeof os.availableParallelism === 'function' ? os.availableParallelism() : 'n/a'}`,
    `memory ${mb(os.totalmem())} total, ${mb(os.freemem())} free`,
  ];
  // cgroup v2 (Linux containers): "max 100000" or "<quota> <period>"
  const cpuMax = read('/sys/fs/cgroup/cpu.max');
  if (cpuMax) {
    const [quota, period] = cpuMax.split(/\s+/);
    parts.push(`cgroup cpu.max ${cpuMax}${quota !== 'max' ? ` (= ${(Number(quota) / Number(period)).toFixed(2)} CPUs)` : ''}`);
  }
  const memMax = read('/sys/fs/cgroup/memory.max');
  if (memMax) parts.push(`cgroup memory.max ${memMax === 'max' ? 'max' : mb(Number(memMax))}`);
  // cgroup v1 fallbacks
  const cfsQuota = read('/sys/fs/cgroup/cpu/cpu.cfs_quota_us');
  const cfsPeriod = read('/sys/fs/cgroup/cpu/cpu.cfs_period_us');
  if (cfsQuota && cfsPeriod && cfsQuota !== '-1') parts.push(`cgroup v1 cpu quota ${(Number(cfsQuota) / Number(cfsPeriod)).toFixed(2)} CPUs`);
  const v1Mem = read('/sys/fs/cgroup/memory/memory.limit_in_bytes');
  if (v1Mem && Number(v1Mem) < 2 ** 60) parts.push(`cgroup v1 memory limit ${mb(Number(v1Mem))}`);
  if (process.env.DOCUSAURUS_SSG_WORKER_THREAD_COUNT) parts.push(`DOCUSAURUS_SSG_WORKER_THREAD_COUNT=${process.env.DOCUSAURUS_SSG_WORKER_THREAD_COUNT}`);
  console.log(`build-env: ${parts.join('; ')}`);
} catch (err) {
  console.log(`build-env: could not read machine info (${err.message})`);
}
