---
title: 什么是基础设施漂移？
description: 基础设施漂移是指云资源的声明状态或记录状态与其在 Provider 中的实际状态之间的偏差，由带外变更、部分失败或平台的自主行为引起。
keywords: [基础设施漂移, 漂移检测, 状态文件, 配置漂移, 调谐, stackql]
proficiencyLevel: Beginner
faq:
  - question: 基础设施漂移是由什么引起的？
    answer: 常见原因包括：事件期间在云控制台中进行的手动更改、绕过 IaC 工具的脚本和流水线、Provider 侧的自动化（自动伸缩、证书轮换、默认策略变更）、失败或部分完成的 apply，以及多个工具管理相互重叠的资源。
  - question: 漂移总是坏事吗？
    answer: 不是。有些漂移是有意为之且健康的，例如自动伸缩改变实例数量，或服务中断期间的 break-glass 应急修复。问题在于未被检测到的漂移：记录的状态不再描述现实，而决策却是依据记录做出的。目标是检测和调谐，而不是消除某一个工具之外的所有变更。
  - question: StackQL 如何检测漂移？
    answer: StackQL 直接查询 Provider API，因此其结果按定义就是实际状态。将 StackQL 结果集与期望状态定义（IaC 清单、CSV 基线或 stackql-deploy 规范）进行比较，即可识别偏差，而无需 IaC 工具自身的状态文件保持准确。
---

# 什么是基础设施漂移？

基础设施漂移是指云资源的声明状态或记录状态与其在 Provider 中的实际状态之间的偏差。Terraform 状态文件记载某个安全组有 3 条规则；而实际运行中的安全组有 5 条，因为有人在某次事件期间通过控制台添加了 2 条。多出的这 2 条规则就是漂移。

## 漂移如何产生 {#how-drift-happens}

漂移是结构性的，而不是偶然的。任何保存状态副本的系统（状态文件、CMDB、资源清单导出）都会与现实发生漂移，其速度等于现实在该系统之外发生变化的速度。典型来源：

- **带外变更**：控制台修改、临时 CLI 命令、紧急修复。
- **并发工具**：2 条流水线或 2 个团队管理相互重叠的资源。
- **Provider 自动化**：自动伸缩、托管升级、密钥和证书轮换。
- **部分失败**：某次 apply 创建了 6 个资源中的 4 个，却一个也没有记录。

IaC 模型把漂移视为一种异常，在下一次 plan/apply 周期中予以纠正。在存在自主行动者（自动伸缩器、事件响应人员、AI 智能体）的环境中，更适合把漂移视为一种正常且持续存在的状况，加以观察和调谐。

## 使用实时查询检测漂移 {#detecting-drift-with-live-queries}

由于 StackQL 根据实时的 Provider API 计算结果，StackQL 结果集就是实际状态。漂移检测由此简化为将该结果集与基线进行比较。例如，按区域获取 EC2 实例的实时资源清单：

```sql
SELECT region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

将其与期望状态定义所隐含的数量进行比较，可以立即发现未受管或缺失的资源，而不论 IaC 工具自身的状态文件是否是最新的。同样的模式也适用于属性级别：查询资源的实时配置，并将其与声明的配置进行差异比较。

## 漂移与调谐 {#drift-vs-reconciliation}

检测回答的是“哪里出现了偏差？”；调谐回答的是“应该如何处理？”：将该变更导入期望状态、将其回退，或将其作为有意为之的变更予以接受。调谐需要对控制平面的写入权限，因此读写型查询工具能够闭合环路，而只读的资源清单工具只能就此出具报告。StackQL 将纠正操作表示为 `UPDATE` 或 `DELETE` 语句，针对的是检测所用的同一批表。

## 相关概念 {#related-concepts}

- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：使实时状态比较成为可能的模型
- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：对实时状态的读写访问
- [为什么状态文件对 AI 智能体失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：漂移是状态副本的结构性属性
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：状态文件模型与实时查询模型的对比
- [如何使用 StackQL 查询 AWS EC2 实例](/ai/how-tos/query-aws-ec2-instances-with-stackql)：漂移基线中使用的资源清单查询
