# 贡献指南

感谢您对淘淘商城的关注！我们欢迎任何形式的贡献，包括但不限于：

- 提交 Bug 报告
- 功能建议
- 代码提交
- 文档改进

## 提交 Issue

在提交 Issue 前，请先搜索现有 Issue 是否已涵盖您的问题。

### Bug 报告

请包含以下信息：

- 运行环境（JDK 版本、操作系统、Tomcat 版本等）
- 复现步骤
- 期望行为和实际行为
- 相关日志和截图

### 功能建议

请描述您期望的功能及其使用场景。

## 代码贡献

### 工作流程

1. Fork 本仓库
2. 从 `master` 分支创建您的功能分支：`git checkout -b feature/your-feature`
3. 提交您的变更：`git commit -m "feat: add some feature"`
4. 推送到您的仓库：`git push origin feature/your-feature`
5. 创建 Pull Request

### Commit 规范

请遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
feat: 新功能
fix: Bug 修复
docs: 文档变更
refactor: 代码重构
perf: 性能优化
test: 测试相关
chore: 构建/工具链变更
```

### 代码规范

- 保持 JDK 1.7 兼容性
- 遵循阿里巴巴 Java 开发规范
- 使用 SLF4J 日志门面，禁止使用 `System.out` 或 `e.printStackTrace()`
- 保持现有的代码风格和命名规范

## Pull Request 流程

1. 确保 PR 描述清晰地说明了变更内容和动机
2. 确保所有单元测试通过
3. 确保新增代码包含必要的日志记录
4. PR 需至少一位维护者 review 后方可合并

## 开发环境

### 必需

- JDK 1.7+
- Maven 3.2+
- MySQL 5.7+
- Tomcat 7+

### 可选

- Redis 3.0+
- Solr 6.6+
- ZooKeeper 3.4+
- ActiveMQ 5.15+
- FastDFS 5.0+

详细安装步骤请参考 [INSTALL.md](INSTALL.md)。
