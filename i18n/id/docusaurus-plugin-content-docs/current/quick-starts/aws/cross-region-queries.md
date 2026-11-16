---
title: Kueri lintas region
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
  - cspm
  - aws
  - amazon web services
description: "Kueri dan agregasikan sumber daya AWS dari beberapa region dalam satu pernyataan StackQL, dengan daftar region dan eksekusi asinkron sebagai pengganti loop per region."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Kueri lintas region asinkron dengan StackQL

Dalam panduan ini, kita akan mendemonstrasikan kemampuan StackQL yang andal: kemampuan untuk mengkueri dan mengagregasi sumber daya di beberapa region AWS.  Hal ini melibatkan banyak permintaan API ke berbagai endpoint, yang dijalankan secara asinkron.  Semua ini berlangsung mulus dan transparan bagi pengguna.  

> StackQL adalah alat pengembangan yang andal untuk mengkueri dan men-deploy infrastruktur dan sumber daya cloud menggunakan sintaksis SQL.  

Diuji dengan <span class="tutorial_tested_on">backend SQL tersemat</span> <span class="tutorial_tested_on">backend SQL postgres</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Memahami region dan sumber daya AWS {#understanding-aws-regions-and-resources}

Sumber daya AWS di-hosting di banyak lokasi di seluruh dunia.  Lokasi ini terdiri atas region dan Availability Zone, yang dirancang agar pengguna dapat menempatkan instance dan menyimpan data di berbagai lokasi.

## Masalah dengan region AWS... {#the-problem-with-aws-regions}

Saat bekerja dengan AWS, penting untuk dipahami bahwa AWS Management Console menyediakan tampilan sumber daya dengan cakupan regional, sehingga pengguna dapat berinteraksi dengan dan mengelola sumber daya dalam satu region tertentu pada satu waktu.  Demikian pula, perintah AWS Command Line Interface (CLI) dan Software Development Kit (SDK) menggunakan region sebagai titik masuk untuk operasi, sehingga region harus ditentukan untuk menjalankan perintah atau program.  

> Struktur regional layanan cloud menimbulkan kompleksitas ketika sumber daya tersebar di beberapa region.  Selain itu, pelanggaran keamanan, termasuk eksploitasi penambangan mata uang kripto, sering memanfaatkan sumber daya di berbagai area, melampaui region utama yang biasanya digunakan oleh organisasi.

## Menentukan region dalam kueri StackQL {#specifying-regions-in-stackql-queries}

Dengan StackQL, Anda dapat menentukan region untuk menargetkan sumber daya di berbagai lokasi geografis.  Hal ini sangat berguna untuk mengelola sumber daya yang tersebar di seluruh dunia.

### Contoh: mencantumkan instance EC2 di beberapa region {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

Kueri ini mengembalikan daftar instance EC2 dari region `us-east-1` (Virginia Utara) dan `eu-west-1` (Irlandia), yang menunjukkan cara StackQL mengambil data lintas region dalam satu kueri.  Cara ini juga dapat digunakan dengan kueri agregat, misalnya:  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## Keunggulan kueri asinkron {#the-power-of-asynchronous-queries}

StackQL menjalankan kueri lintas region ini secara asinkron dengan mengirim beberapa permintaan API paralel ke AWS.  Hal ini memastikan pengambilan data yang efisien tanpa menunggu permintaan untuk satu region selesai sebelum beralih ke region berikutnya.

## Kesimpulan {#conclusion}

Kueri lintas region asinkron di StackQL menyediakan metode yang tangguh dan efisien untuk mengelola sumber daya AWS di berbagai lokasi geografis.  Dengan memanfaatkan sintaksis mirip SQL untuk kueri ini, StackQL menyederhanakan tugas pengelolaan cloud yang kompleks, sehingga menjadi alat yang sangat berharga bagi pengembang dan administrator cloud yang ingin mengoptimalkan infrastruktur AWS mereka.
