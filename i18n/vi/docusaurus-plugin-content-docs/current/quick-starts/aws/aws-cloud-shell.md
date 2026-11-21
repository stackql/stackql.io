---
title: StackQL trong AWS CloudShell
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
  - cspm
  - aws
  - amazon web services
description: "Chạy StackQL trong AWS CloudShell với tư cách người dùng đã đăng nhập: tải xuống gói Linux, dùng stackql shell, đảm nhận vai trò IAM và chạy truy vấn theo lô bằng exec."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Sử dụng StackQL trong AWS CloudShell

Trong hướng dẫn này, chúng ta sẽ minh họa cách sử dụng StackQL, một công cụ phát triển mạnh mẽ cho phép truy vấn và triển khai hạ tầng và tài nguyên đám mây bằng cú pháp SQL ngay trong AWS CloudShell, sử dụng ngữ cảnh người dùng của phiên đăng nhập vào AWS Console hoặc đảm nhận một vai trò IAM khác.

Đã kiểm thử với <span class="tutorial_tested_on">backend SQL nhúng</span> <span class="tutorial_tested_on">linux</span>

<details>

<summary><b>AWS CloudShell</b> là gì?</summary>

> AWS CloudShell là một dịch vụ shell chạy trên trình duyệt, được xác thực sẵn, do Amazon Web Services (AWS) cung cấp.  Dịch vụ này cho phép nhà phát triển và chuyên gia công nghệ thông tin quản lý tài nguyên đám mây trực tiếp từ trình duyệt web.  Với AWS CloudShell, người dùng có thể nhanh chóng chạy các lệnh liên quan đến dịch vụ AWS mà không cần cài đặt hoặc cấu hình AWS CLI (Command Line Interface) trên máy tính cục bộ.  Dịch vụ này cung cấp một môi trường sẵn sàng sử dụng với các công cụ phát triển và quản lý thông dụng được cài đặt sẵn, chẳng hạn như AWS CLI, Python, Node.js và nhiều công cụ khác, giúp việc tương tác với dịch vụ AWS và tự động hóa tác vụ trở nên dễ dàng hơn.  AWS CloudShell được tích hợp trực tiếp với AWS Management Console, mang lại trải nghiệm liền mạch khi thực hiện các tác vụ quản trị, khám phá dịch vụ AWS và quản lý hạ tầng.  Vì AWS CloudShell tự động xác thực người dùng thông qua phiên đăng nhập AWS Console, dịch vụ này đơn giản hóa quá trình chạy lệnh và tập lệnh trong ngữ cảnh tài khoản AWS của họ, nâng cao năng suất và hiệu quả trong vận hành đám mây.

</details>

## Cách hoạt động {#how-it-works}

Gói linux của StackQL bao gồm các tập lệnh để sử dụng `stackql` trực tiếp trong môi trường shell đám mây tích hợp sẵn của các nhà cung cấp đám mây lớn.  Với AWS, tập lệnh là __`stackql-aws-cloud-shell.sh`__.  Cách dùng tập lệnh __`stackql-aws-cloud-shell.sh`__ được trình bày dưới đây:

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

Tập lệnh `stackql-aws-cloud-shell.sh` tự động tải xuống provider AWS mới nhất cho StackQL.  Mã nguồn của các tập lệnh shell đám mây đi kèm có tại [__github.com/stackql/cloud-shell-scripts__](https://github.com/stackql/cloud-shell-scripts).

## Tải xuống gói Linux `stackql` từ AWS CloudShell {#download-the-stackql-linux-package-from-aws-cloud-shell}

Để bắt đầu, từ AWS CloudShell, hãy tải xuống tệp nhị phân Linux mới nhất của StackQL bằng các lệnh sau:

```shell
curl -L https://bit.ly/stackql-zip -O \
&& unzip stackql-zip
```

Các lệnh này sẽ tải xuống và giải nén tệp nhị phân `stackql` cùng với tập lệnh `stackql-aws-cloud-shell.sh` vào thư mục home của môi trường AWS CloudShell của bạn.  Giờ bạn đã sẵn sàng!

## Sử dụng `stackql shell` từ AWS CloudShell {#using-the-stackql-shell-from-aws-cloud-shell}

Khởi chạy một shell lệnh StackQL đã được xác thực từ bên trong AWS CloudShell chỉ đơn giản như sau:

```shell
sh stackql-aws-cloud-shell.sh
```

Giờ bạn có thể bắt đầu chạy các truy vấn AWS dựa trên SQL một cách tương tác bằng provider StackQL, như minh họa dưới đây:

<video width="720" height="480" controls>
  <source src="/img/tutorials/aws/stackql-aws-cloud-shell.mp4" type="video/mp4" />
  Trình duyệt của bạn không hỗ trợ thẻ video.
</video>

## Đảm nhận vai trò IAM bằng StackQL {#assuming-an-iam-role-using-stackql}

Đảm nhận một vai trò IAM trong AWS cho phép bạn thực hiện các hành động và truy cập tài nguyên mà bạn có thể không làm được với quyền người dùng mặc định.  Điều này đặc biệt hữu ích khi bạn cần quyền truy cập tạm thời vào tài nguyên hoặc cần thực hiện hành động với các quyền khác với tài khoản chính của bạn.  Với StackQL, bạn có thể đảm nhận một vai trò IAM một cách liền mạch để truy vấn và quản lý tài nguyên AWS theo các quyền được cấp cho vai trò đó.

<details>
<summary>Tìm hiểu về việc đảm nhận vai trò IAM</summary>

> Đảm nhận một vai trò IAM là một cách an toàn để cho phép truy cập tạm thời vào tài nguyên AWS.  Khi bạn đảm nhận một vai trò, AWS STS cung cấp cho bạn thông tin xác thực bảo mật tạm thời, cho phép bạn truy cập các dịch vụ và tài nguyên AWS mà vai trò đó có quyền sử dụng.  Cơ chế này lý tưởng cho truy cập liên tài khoản, truy cập tạm thời cho người dùng hoặc tuân thủ nguyên tắc đặc quyền tối thiểu.

> Có thể đảm nhận vai trò bằng AWS Management Console, AWS CLI hoặc AWS SDK, và trực tiếp trong StackQL. Điều này đảm bảo người dùng và ứng dụng chỉ có các quyền cần thiết trong khoảng thời gian cần thiết, giúp nâng cao mức độ bảo mật của môi trường AWS của bạn.

</details>

Để đảm nhận một vai trò IAM bằng StackQL trong AWS CloudShell, bạn có thể dùng tùy chọn `--role-arn` với tập lệnh `stackql-aws-cloud-shell.sh`.  Tùy chọn này yêu cầu Amazon Resource Name (ARN) của vai trò bạn muốn đảm nhận.  Khi tùy chọn này được cung cấp, StackQL sẽ dùng AWS Security Token Service (STS) để đảm nhận vai trò đã chỉ định trước khi thực thi bất kỳ truy vấn hoặc lệnh nào.

```shell
sh stackql-aws-cloud-shell.sh --role-arn arn:aws:iam::123456789012:role/YourRoleName
```

Bằng cách đảm nhận một vai trò, bạn có thể tận dụng StackQL để tương tác với các tài nguyên AWS mà người dùng mặc định của bạn có thể không có quyền truy cập, mang lại một cách quản lý môi trường đám mây linh hoạt và an toàn hơn.

Khi thực thi các lệnh hoặc tập lệnh StackQL với một vai trò IAM được đảm nhận, hãy đảm bảo vai trò đó có các quyền cần thiết để thực hiện các hành động mong muốn và truy cập các tài nguyên đã chỉ định.  Cách tiếp cận này hỗ trợ kiểm soát truy cập chi tiết và phù hợp với các phương pháp hay nhất của AWS về bảo mật và quản trị.

## Chạy `stackql exec` (thao tác theo lô) từ AWS CloudShell {#running-stackql-exec-batch-operations-from-aws-cloud-shell}

Truy vấn StackQL cũng có thể được thực thi bằng lệnh `exec`; đây là một lựa chọn hữu ích để:

- chỉ định các định dạng đầu ra khác như `json` hoặc `csv`
- tạo tệp đầu ra cho kết quả của truy vấn StackQL
- sử dụng truy vấn StackQL được cung cấp qua tệp đầu vào (theo quy ước là tệp `.iql`)
- thực hiện các thao tác phức tạp, bao gồm nạp biến hoặc tiền xử lý truy vấn (bằng `json` hoặc `jsonnet`)

Ví dụ sử dụng `exec` trong AWS CloudShell được trình bày dưới đây:

```shell
sh stackql-aws-cloud-shell.sh exec \
--output csv --outfile instances.csv \
"SELECT region, instance_type FROM aws.ec2.instances WHERE region IN ('us-east-1')"
```

Ví dụ trên chạy truy vấn StackQL đã chỉ định và tạo ra một tệp đầu ra `CSV`, có thể tải xuống từ AWS CloudShell.  Ngoài ra, bạn có thể cung cấp một vai trò IAM bằng đối số `--role-arn` để đảm nhận một vai trò khác cho truy vấn hoặc thao tác thay đổi dữ liệu của bạn; ví dụ được trình bày dưới đây (trong trường hợp này, chúng ta lấy truy vấn từ một tệp đầu vào):

```shell
sh stackql-aws-cloud-shell.sh \
--role-arn arn:aws:iam::123456789012:role/SecurityReviewerRole exec \
--infile query.iql \
--output csv --outfile output.csv
```

Bạn có thể tìm thêm thông tin về lệnh `exec` [__tại đây__](/command-line-usage/exec).
