---
title: SQL untuk infrastruktur cloud, API SaaS, dan agen AI
sidebar_label: Selamat datang di StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: StackQL adalah mesin sumber terbuka bagi manusia dan agen AI untuk mengkueri, menyediakan, dan mengoperasikan layanan cloud dan SaaS dengan SQL, tanpa file status atau SDK per cloud.
image: "/img/stackql-featured-image.png"
slug: /
# SoftwareApplication node on the homepage, from the site-wide defaults in
# themeConfig.structuredData.softwareApplication (name, category, OS,
# license, download URL, free offer)
softwareApplication: true
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import DocsHero from '@site/src/components/DocsHero/DocsHero';
import { FaRobot, FaCode, FaSatelliteDish } from 'react-icons/fa';

<DocsHero
  lightSrc="/img/stackql-logo-bold-light-mode.png"
  darkSrc="/img/stackql-logo-bold-dark-mode.png"
  alt="StackQL"
  title={
  <>
  Antarmuka universal<br/>
  untuk era agentik
  </>
  }
  byline={
    <>
    Configuration-as-Data. Infrastructure-as-Context.
    </>
    }
/>

---

<div className="row margin-top--lg margin-bottom--lg">
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaRobot size={22} className="homeCardIcon" />Rekayasa platform agentik</h3>
      </div>
      <div className="card__body">
        Permukaan universal bagi agen dan manusia untuk menyusun perubahan secara berdampingan, dengan kecepatan mesin. Banyak penulis sekaligus, status terkini, dan eksekusi yang dibatasi kebijakan sebagai aspek utama - fondasi yang dibutuhkan sistem otonom untuk beroperasi dengan aman.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC untuk era agentik</h3>
      </div>
      <div className="card__body">
        Kode tetap menyatakan niat. Realitas adalah data. StackQL memberi agen dan operator satu antarmuka deklaratif untuk mengkueri status terkini di berbagai provider, menegaskan status yang diinginkan secara idempoten, dan menelusuri bidang kontrol dan bidang data melalui satu tata bahasa - tanpa file status, tanpa SDK per cloud.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Observabilitas otonom</h3>
      </div>
      <div className="card__body">
        Perlakukan API cloud sebagai lapisan data Anda, bukan target deployment Anda. Setiap pembacaan bersifat langsung, setiap perubahan adalah peristiwa, dan penyimpangan menjadi sinyal yang dapat dinalar oleh agen - bukan alarm yang harus dikejar manusia.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Kueri', value: 'iql', },
    { label: 'Hasil', value: 'data', },
  ]
}>
<TabItem value="iql">

```sql
SELECT state, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY state;
```

</TabItem>
<TabItem value="data">

```
|---------------------------------|
|      state      | num_instances |
|---------------------------------|
|     running     |     342       |
|---------------------------------|
|     pending     |       4       |
|---------------------------------|
|     stopped     |      38       |
|---------------------------------|
```

</TabItem>
</Tabs>

Men-deploy sumber daya di penyedia cloud Anda semudah menulis pernyataan [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Dengan StackQL, Anda dapat mengembangkan dengan cara Anda sendiri: deklaratif atau prosedural. Dengan tata bahasa yang mudah dipelajari dan tanpa file status yang perlu dikelola, Anda dapat memulai dengan cepat dan menggunakan StackQL secara bergantian dengan alat infrastruktur sebagai kode lainnya, baik cloud native maupun bukan. StackQL menyediakan antarmuka universal bagi agen AI untuk berinteraksi dengan infrastruktur cloud. Kegunaan StackQL antara lain:

- **Integrasi agen AI** - Memungkinkan agen menyediakan dan mengkueri sumber daya cloud
- **Komunikasi bidang kontrol** - Penyediaan infrastruktur secara terprogram untuk workflow agentik
- **Observabilitas bidang data** - Pemantauan dan analitik waktu nyata untuk operasi berbasis AI
- Deployment infrastruktur cloud (menggunakan SQL)
- Inventaris aset cloud dan pelaporan (menggunakan SQL)
- Kepatuhan cloud dan atestasi kontrol (menggunakan SQL)
- Deteksi penyimpangan konfigurasi (menggunakan SQL)
- dan banyak lagi, hanya dibatasi oleh imajinasi Anda!

## Untuk siapa StackQL {#who-stackql-is-for}

StackQL ditujukan untuk tim rekayasa platform dan SRE yang mengoperasikan lingkungan cloud, tim keamanan dan FinOps yang membutuhkan jawaban terkini tentang inventaris, kepatuhan, dan biaya, data engineer yang ingin menjadikan API cloud dan SaaS sebagai sumber yang dapat dikueri, serta pengembang yang membangun agen AI yang harus mengkueri dan mengubah sumber daya cloud dan SaaS dengan aman. StackQL berjalan di mana pun pekerjaan dilakukan: sebagai biner tunggal di macOS, Linux, dan Windows, di Docker, di CI dengan GitHub Actions, di shell AWS, Azure, dan Google Cloud, tersemat dalam aplikasi, sebagai server yang kompatibel dengan PostgreSQL untuk alat BI dan notebook, serta sebagai server MCP untuk agen AI.

## Lisensi dan dukungan {#license-and-support}

StackQL bersifat sumber terbuka di bawah [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) dan gratis digunakan untuk tujuan apa pun. Dukungan komunitas tersedia melalui [GitHub Discussions](https://github.com/orgs/stackql/discussions) dan [Discord](https://discord.com/invite/xVXZ9d5NxN); bug dan permintaan fitur diajukan ke [GitHub Issues](https://github.com/stackql/stackql/issues). Untuk pertanyaan tentang dukungan komersial, [hubungi StackQL Studios](/contact-us).

## Mulai sekarang {#get-started}

- [Instalasi](/installing-stackql) - instal biner StackQL di macOS, Linux, atau Windows, atau jalankan dari Docker, manajer paket, atau shell cloud
- [Agen AI](/ai-agents) - hubungkan agen AI melalui MCP, sematkan server, dan jelajahi kueri serta konten referensi
- [Memulai](/getting-started) - hierarki sumber daya, kueri pertama Anda, menggunakan provider, mode output, variabel, dan penggunaan template
- [Provider yang tersedia](/providers) - provider cloud dan SaaS yang dapat Anda kueri dan kelola, masing-masing dengan situs referensinya sendiri
- [Blog](/blog) - pengumuman produk, pengumuman provider, dan tutorial
- [Baris perintah](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry`, dan flag global
- [Alat MCP](/mcp) - server dan alat Model Context Protocol yang memberi agen AI antarmuka SQL yang sama
- [Spesifikasi bahasa](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, fungsi, dan tipe data
