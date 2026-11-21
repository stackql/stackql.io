---
title: StackQL di AWS CloudShell
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
description: "Jalankan StackQL di AWS CloudShell sebagai pengguna yang sedang login: unduh paket Linux, gunakan shell stackql, ambil peran IAM, dan jalankan kueri batch dengan exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Menggunakan StackQL di AWS CloudShell

Dalam panduan ini, kita akan mendemonstrasikan penggunaan StackQL, alat pengembangan andal yang memungkinkan Anda mengkueri dan men-deploy infrastruktur dan sumber daya cloud dengan sintaksis SQL, dari dalam AWS CloudShell, menggunakan konteks pengguna dari sesi login ke AWS Console atau dengan mengambil peran IAM lain.    

Diuji dengan <span class="tutorial_tested_on">backend SQL tersemat</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary>Apa itu <b>AWS CloudShell</b>?</summary>

> AWS CloudShell adalah layanan shell berbasis browser yang sudah diautentikasi sebelumnya, yang disediakan oleh Amazon Web Services (AWS).  Layanan ini memungkinkan pengembang dan profesional TI mengelola sumber daya cloud mereka langsung dari browser web.  Dengan AWS CloudShell, pengguna dapat dengan cepat menjalankan perintah yang terkait dengan layanan AWS tanpa menginstal maupun mengonfigurasi AWS CLI (Command Line Interface) di mesin lokal mereka.  Layanan ini menyediakan lingkungan siap pakai dengan alat pengembangan dan manajemen umum yang sudah terinstal, seperti AWS CLI, Python, Node.js, dan lainnya, sehingga memudahkan interaksi dengan layanan AWS dan otomatisasi tugas.  AWS CloudShell terintegrasi langsung dengan AWS Management Console dan memberikan pengalaman yang mulus untuk menjalankan tugas administratif, menjelajahi layanan AWS, dan mengelola infrastruktur.  Karena AWS CloudShell secara otomatis mengautentikasi pengguna melalui login AWS Console mereka, layanan ini menyederhanakan proses menjalankan perintah dan skrip dalam konteks akun AWS mereka, sehingga meningkatkan produktivitas dan efisiensi operasi cloud.

</details>

## Cara kerja {#how-it-works}

Paket linux StackQL menyertakan skrip untuk menggunakan `stackql` langsung di lingkungan cloud shell bawaan milik penyedia cloud utama.  Untuk AWS, skripnya adalah __`stackql-aws-cloud-shell.sh`__.  Penggunaan skrip __`stackql-aws-cloud-shell.sh`__ ditunjukkan di sini:  

```
Usage:

  ./stackql-aws-cloud-shell.sh [--role-arn ARN] [shell | exec] [flags]

  --role-arn (optional) 
      If supplied, the program will assume the role specified;
        if not, then the current user context in the cloud shell will be used.
  
  Command (optional):
      'shell' (default) enters the StackQL command shell to execute queries interactively.
      'exec' is used to execute StackQL queries or files to provide batch outputs
        (such as CSV or JSON output files).  If not specified, 'shell' is assumed.

  Flags:
      optional StackQL flags, documented in Command Line Usage -> Global Flags
```

Skrip `stackql-aws-cloud-shell.sh` secara otomatis mengunduh provider AWS terbaru untuk StackQL.  Kode sumber skrip cloud shell yang disertakan dapat ditemukan di [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).  

## Mengunduh paket Linux `stackql` dari AWS CloudShell {#download-the-stackql-linux-package-from-aws-cloud-shell}

Untuk memulai, dari AWS CloudShell, unduh biner Linux terbaru untuk StackQL dengan perintah berikut:  

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Perintah ini akan mengunduh dan mengekstrak biner `stackql` beserta skrip `stackql-aws-cloud-shell.sh` ke direktori home lingkungan AWS CloudShell Anda.  Sekarang Anda siap!  

## Menggunakan `stackql shell` dari AWS CloudShell {#using-the-stackql-shell-from-aws-cloud-shell}

Meluncurkan shell perintah StackQL yang sudah diautentikasi dari dalam AWS CloudShell cukup dengan:  

```shell
sh stackql-aws-cloud-shell.sh
```

Sekarang Anda dapat mulai menjalankan kueri AWS interaktif berbasis SQL menggunakan provider StackQL, seperti yang ditunjukkan di sini:  

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Browser Anda tidak mendukung tag video.
</video>

## Mengambil peran IAM menggunakan StackQL {#assuming-an-iam-role-using-stackql}

Mengambil peran IAM di AWS memungkinkan Anda melakukan tindakan dan mengakses sumber daya yang mungkin tidak dapat Anda lakukan dengan izin pengguna default Anda.  Hal ini sangat berguna saat Anda memerlukan akses sementara ke sumber daya atau perlu melakukan tindakan dengan izin yang berbeda dari akun utama Anda.  Dengan StackQL, Anda dapat dengan mudah mengambil peran IAM untuk mengkueri dan mengelola sumber daya AWS dengan izin yang diberikan oleh peran tersebut.

<details>
<summary>Memahami pengambilan peran IAM</summary>

> Mengambil peran IAM merupakan cara yang aman untuk memberikan akses sementara ke sumber daya AWS.  Saat Anda mengambil peran, AWS STS memberi Anda kredensial keamanan sementara yang memungkinkan Anda mengakses layanan dan sumber daya AWS yang diizinkan untuk digunakan oleh peran tersebut.  Mekanisme ini ideal untuk akses lintas akun, akses sementara bagi pengguna, atau penerapan prinsip hak istimewa minimum.

> Peran dapat diambil menggunakan AWS Management Console, AWS CLI, atau AWS SDK, dan langsung di dalam StackQL. Hal ini memastikan pengguna dan aplikasi hanya memiliki izin yang diperlukan selama durasi yang dibutuhkan, sehingga memperkuat postur keamanan lingkungan AWS Anda.

</details>

Untuk mengambil peran IAM menggunakan StackQL di AWS CloudShell, Anda dapat menggunakan opsi `--role-arn` dengan skrip `stackql-aws-cloud-shell.sh`.  Opsi ini memerlukan Amazon Resource Name (ARN) dari peran yang ingin Anda ambil.  Jika opsi ini diberikan, StackQL akan menggunakan AWS Security Token Service (STS) untuk mengambil peran yang ditentukan sebelum menjalankan kueri atau perintah apa pun.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

Dengan mengambil peran, Anda dapat memanfaatkan StackQL untuk berinteraksi dengan sumber daya AWS yang mungkin tidak dapat diakses oleh pengguna default Anda, sehingga Anda dapat mengelola lingkungan cloud dengan cara yang lebih fleksibel dan aman.  

Saat menjalankan perintah atau skrip StackQL dengan peran IAM yang diambil, pastikan peran tersebut memiliki izin yang diperlukan untuk melakukan tindakan yang diinginkan dan mengakses sumber daya yang ditentukan.  Pendekatan ini memudahkan kontrol akses yang granular dan selaras dengan praktik terbaik AWS untuk keamanan dan tata kelola.  

## Menjalankan `stackql exec` (operasi batch) dari AWS CloudShell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

Kueri StackQL juga dapat dijalankan menggunakan perintah `exec`; ini merupakan opsi yang berguna untuk:

- menentukan format output alternatif seperti `json` atau `csv`
- menghasilkan file output untuk hasil kueri StackQL
- menggunakan kueri StackQL yang disediakan melalui file input (secara konvensi, file `.iql`)
- melakukan operasi kompleks, termasuk memuat variabel atau memproses kueri terlebih dahulu (menggunakan `json` atau `jsonnet`)

Contoh penggunaan `exec` di AWS CloudShell ditunjukkan di sini:  

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instance_type FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

Contoh di atas menjalankan kueri StackQL yang ditentukan dan menghasilkan file output `CSV`, yang dapat diunduh dari AWS CloudShell.  Selain itu, Anda dapat memberikan peran IAM menggunakan argumen `--role-arn` untuk mengambil peran lain bagi kueri atau operasi mutasi Anda; contohnya ditunjukkan di sini (dalam hal ini, kita mengambil kueri dari file input):  

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Informasi selengkapnya tentang perintah `exec` dapat ditemukan [__di sini__](/command-line-usage/exec).
