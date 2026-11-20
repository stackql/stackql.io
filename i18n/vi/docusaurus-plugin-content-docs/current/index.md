---
title: SQL cho hạ tầng đám mây, API SaaS và tác nhân AI
sidebar_label: Chào mừng đến với StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: StackQL là công cụ mã nguồn mở cho phép con người và tác nhân AI truy vấn, cấp phát và vận hành dịch vụ đám mây và SaaS bằng SQL, không cần tệp trạng thái hay SDK riêng cho từng đám mây.
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
  Giao diện phổ quát<br/>
  cho kỷ nguyên tác nhân AI
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
        <h3><FaRobot size={22} className="homeCardIcon" />Kỹ thuật nền tảng dựa trên tác nhân</h3>
      </div>
      <div className="card__body">
        Một bề mặt phổ quát để tác nhân và con người cùng soạn thay đổi song song, ở tốc độ máy. Nhiều bên ghi đồng thời, trạng thái trực tiếp và thực thi trong giới hạn chính sách là những yếu tố hàng đầu - nền tảng mà các hệ thống tự trị cần để vận hành an toàn.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />IaC cho kỷ nguyên tác nhân AI</h3>
      </div>
      <div className="card__body">
        Mã vẫn là ý định. Thực tế là dữ liệu. StackQL cung cấp cho tác nhân và người vận hành một giao diện khai báo duy nhất để truy vấn trạng thái trực tiếp trên nhiều provider, khẳng định trạng thái mong muốn một cách lũy đẳng và duyệt qua mặt phẳng điều khiển và mặt phẳng dữ liệu bằng một ngữ pháp duy nhất - không tệp trạng thái, không SDK riêng cho từng đám mây.
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />Khả năng quan sát tự trị</h3>
      </div>
      <div className="card__body">
        Hãy coi API đám mây là lớp dữ liệu của bạn, không phải đích triển khai. Mọi thao tác đọc đều lấy dữ liệu trực tiếp, mọi thay đổi đều là một sự kiện và sai lệch trở thành tín hiệu mà tác nhân có thể suy luận - không phải một báo động mà con người phải chạy theo.
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Truy vấn', value: 'iql', },
    { label: 'Kết quả', value: 'data', },
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

Triển khai tài nguyên trên nhà cung cấp đám mây của bạn dễ dàng như viết một câu lệnh [`INSERT`](/language-spec/insert)...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Với StackQL, bạn có thể phát triển theo cách của mình: khai báo hoặc thủ tục. Với ngữ pháp dễ học và không có tệp trạng thái cần quản lý, bạn có thể bắt đầu nhanh chóng và dùng StackQL hoán đổi với các công cụ hạ tầng dưới dạng mã khác, dù là công cụ gốc đám mây hay không. StackQL cung cấp một giao diện phổ quát để tác nhân AI tương tác với hạ tầng đám mây. Các ứng dụng của StackQL bao gồm:

- **Tích hợp tác nhân AI** - Cho phép tác nhân cấp phát và truy vấn tài nguyên đám mây
- **Giao tiếp mặt phẳng điều khiển** - Cấp phát hạ tầng theo chương trình cho quy trình làm việc dựa trên tác nhân
- **Khả năng quan sát mặt phẳng dữ liệu** - Giám sát và phân tích theo thời gian thực cho vận hành dựa trên AI
- Triển khai hạ tầng đám mây (bằng SQL)
- Kiểm kê tài sản đám mây và báo cáo (bằng SQL)
- Tuân thủ đám mây và chứng nhận kiểm soát (bằng SQL)
- Phát hiện sai lệch cấu hình (bằng SQL)
- và nhiều hơn nữa, chỉ giới hạn bởi trí tưởng tượng của bạn!

## StackQL dành cho ai {#who-stackql-is-for}

StackQL dành cho các nhóm kỹ thuật nền tảng và SRE vận hành môi trường đám mây, các nhóm bảo mật và FinOps cần câu trả lời trực tiếp về kiểm kê, tuân thủ và chi phí, các kỹ sư dữ liệu muốn dùng API đám mây và SaaS làm nguồn có thể truy vấn, cũng như các nhà phát triển xây dựng tác nhân AI cần truy vấn và thay đổi tài nguyên đám mây và SaaS một cách an toàn. StackQL chạy ở bất cứ nơi nào công việc diễn ra: dưới dạng một tệp nhị phân duy nhất trên macOS, Linux và Windows, trong Docker, trong CI với GitHub Actions, trong shell của AWS, Azure và Google Cloud, nhúng trong ứng dụng, dưới dạng máy chủ tương thích PostgreSQL cho công cụ BI và notebook, cũng như dưới dạng máy chủ MCP cho tác nhân AI.

## Giấy phép và hỗ trợ {#license-and-support}

StackQL là mã nguồn mở theo [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) và được sử dụng miễn phí cho mọi mục đích. Hỗ trợ cộng đồng được cung cấp qua [GitHub Discussions](https://github.com/orgs/stackql/discussions) và [Discord](https://discord.com/invite/xVXZ9d5NxN); lỗi và yêu cầu tính năng được gửi đến [GitHub Issues](https://github.com/stackql/stackql/issues). Đối với yêu cầu hỗ trợ thương mại, hãy [liên hệ StackQL Studios](/contact-us).

## Bắt đầu ngay {#get-started}

- [Cài đặt](/installing-stackql) - cài đặt tệp nhị phân StackQL trên macOS, Linux hoặc Windows, hoặc chạy từ Docker, trình quản lý gói hoặc shell đám mây
- [Tác nhân AI](/ai-agents) - kết nối tác nhân AI qua MCP, nhúng máy chủ và khám phá truy vấn cùng nội dung tham khảo
- [Bắt đầu](/getting-started) - phân cấp tài nguyên, các truy vấn đầu tiên của bạn, sử dụng provider, chế độ đầu ra, biến và sử dụng template
- [Các provider hiện có](/providers) - các provider cho đám mây và SaaS mà bạn có thể truy vấn và quản lý, mỗi provider có trang web tham khảo riêng
- [Blog](/blog) - thông báo sản phẩm, thông báo provider và hướng dẫn
- [Dòng lệnh](/command-line-usage) - `exec`, `shell`, `srv`, `mcp`, `registry` và cờ toàn cục
- [Công cụ MCP](/mcp) - máy chủ và công cụ Model Context Protocol cung cấp cho tác nhân AI cùng một giao diện SQL
- [Đặc tả ngôn ngữ](/language-spec/select) - `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `SHOW`, `DESCRIBE`, hàm và kiểu dữ liệu
