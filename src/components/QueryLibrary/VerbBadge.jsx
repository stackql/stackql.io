import React from 'react';

const VERB_CLASS = {
  select: 'badge--success',
  mutation: 'badge--warning',
  lifecycle: 'badge--info',
};

export default function VerbBadge({verb}) {
  const cls = VERB_CLASS[verb] || 'badge--secondary';
  return <span className={`badge ${cls}`}>{verb}</span>;
}

export function StatusBadge({status}) {
  if (!status || status === 'stable') return null;
  const cls = status === 'deprecated' ? 'badge--danger' : 'badge--secondary';
  return <span className={`badge ${cls}`}>{status}</span>;
}
