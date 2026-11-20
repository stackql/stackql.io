---
title: Truy vấn liên khu vực
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
description: "Truy vấn và tổng hợp tài nguyên AWS trên nhiều khu vực trong một câu lệnh StackQL, với danh sách khu vực và thực thi bất đồng bộ thay vì vòng lặp theo từng khu vực."
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# Truy vấn liên khu vực bất đồng bộ với StackQL

Trong hướng dẫn này, chúng ta sẽ minh họa một khả năng mạnh mẽ của StackQL: khả năng truy vấn và tổng hợp tài nguyên trên nhiều khu vực trong AWS. Việc này cần đến rất nhiều yêu cầu API gửi tới các điểm cuối khác nhau, và các yêu cầu đó được thực hiện bất đồng bộ. Quá trình này diễn ra liền mạch và trong suốt đối với người dùng.

> StackQL là một công cụ mạnh mẽ dành cho nhà phát triển, cho phép truy vấn và triển khai hạ tầng và tài nguyên đám mây bằng cú pháp SQL.

Đã kiểm thử với <span class="tutorial_tested_on">backend SQL nhúng</span> <span class="tutorial_tested_on">backend SQL postgres</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## Tìm hiểu khu vực và tài nguyên AWS {#understanding-aws-regions-and-resources}

Tài nguyên AWS được lưu trữ ở nhiều địa điểm trên toàn thế giới. Các địa điểm này gồm các khu vực (Region) và Availability Zone, được thiết kế để người dùng có thể đặt instance và lưu trữ dữ liệu ở nhiều địa điểm khác nhau.

## Vấn đề với khu vực AWS... {#the-problem-with-aws-regions}

Khi làm việc với AWS, điều quan trọng là phải hiểu rằng AWS Management Console hiển thị tài nguyên theo phạm vi khu vực, cho phép người dùng tương tác và quản lý tài nguyên trong một khu vực cụ thể tại một thời điểm. Tương tự, các lệnh AWS Command Line Interface (CLI) và các Software Development Kit (SDK) dùng khu vực làm điểm vào cho các thao tác, do đó cần chỉ định một khu vực để thực thi lệnh hoặc chạy chương trình.

> Mô hình phân chia theo khu vực của các dịch vụ đám mây gây ra sự phức tạp khi tài nguyên trải rộng trên nhiều khu vực. Ngoài ra, các vụ vi phạm bảo mật, bao gồm cả các cuộc tấn công khai thác để đào tiền mã hóa, thường sử dụng tài nguyên ở nhiều khu vực khác nhau, vượt ra ngoài khu vực chính mà các tổ chức thường sử dụng.

## Chỉ định khu vực trong truy vấn StackQL {#specifying-regions-in-stackql-queries}

Với StackQL, bạn có thể chỉ định các khu vực để nhắm tới tài nguyên ở nhiều vị trí địa lý khác nhau. Điều này đặc biệt hữu ích khi quản lý tài nguyên phân bố trên toàn cầu.

### Ví dụ: liệt kê instance EC2 trên nhiều khu vực {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

Truy vấn này trả về danh sách instance EC2 từ các khu vực `us-east-1` (N. Virginia) và `eu-west-1` (Ireland), cho thấy StackQL có thể lấy dữ liệu trên nhiều khu vực trong một truy vấn duy nhất. Cách này cũng có thể dùng với các truy vấn tổng hợp, chẳng hạn:

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

## Sức mạnh của truy vấn bất đồng bộ {#the-power-of-asynchronous-queries}

StackQL thực thi các truy vấn liên khu vực này một cách bất đồng bộ, gửi nhiều yêu cầu API song song tới AWS. Nhờ đó, việc truy xuất dữ liệu diễn ra hiệu quả mà không phải chờ yêu cầu của một khu vực hoàn tất rồi mới chuyển sang khu vực tiếp theo.

## Kết luận {#conclusion}

Truy vấn liên khu vực bất đồng bộ trong StackQL mang lại một cách thức mạnh mẽ và hiệu quả để quản lý tài nguyên AWS ở nhiều vị trí địa lý khác nhau. Nhờ dùng cú pháp giống SQL cho các truy vấn này, StackQL đơn giản hóa các tác vụ quản lý đám mây phức tạp, trở thành một công cụ vô cùng hữu ích cho các nhà phát triển và quản trị viên đám mây muốn tối ưu hóa hạ tầng AWS của mình.
