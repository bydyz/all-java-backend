# IDEA Markdown 编辑快捷方式总结

## 1. 基础编辑快捷方式

| 快捷键 | 功能描述 |
|--------|----------|
| Tab / Shift+Tab | 缩进/取消缩进 |
| Shift+Enter | 在光标下方新建行 |
| Ctrl+Enter | 在光标上方新建行 |
| Ctrl+Backspace | 删除光标前一个单词 |
| Ctrl+Delete | 删除光标后一个单词 |
| Ctrl+Y | 删除当前行 |
| Ctrl+Z/Y | 撤销/重做 |
| Ctrl+D | 复制当前行/选中文本 |
| Ctrl+Shift+V | 粘贴历史记录 |
| Ctrl+Shift+C | 复制多行文本 |
| Ctrl+Shift+X | 查找最近的操作历史 |

## 2. Markdown 语法快捷方式

### 自动生成语法
输入以下字符后按 Tab 键或回车：

| 输入内容 | 生成的语法 |
|----------|------------|
| # + Tab | 标题 1 |
| ## + Tab | 标题 2 |
| ### + Tab | 标题 3 |
| #### + Tab | 标题 4 |
| * + Tab | 无序列表项 |
| - + Tab | 无序列表项 |
| 1. + Tab | 有序列表项 |
| > + Tab | 引用 |
| ``` + Tab | 代码块 |
| ~~~ + Tab | 代码块 |
| - [ ] + Tab | 待办事项 |
| - [x] + Tab | 已完成事项 |
| ``` + Tab | 代码块 (带语言指定) |

### 格式化快捷方式
| 快捷键 | 功能描述 |
|--------|----------|
| Ctrl+B | 粗体 (`**文本**`) |
| Ctrl+I | 斜体 (`*文本*`) |
| Ctrl+K | 删除线 (`~~文本~~`) |
| Ctrl+Shift+I | 行内代码 (`` `文本` ``) |
| Ctrl+1 | 一级标题 |
| Ctrl+2 | 二级标题 |
| Ctrl+3 | 三级标题 |
| Ctrl+4 | 四级标题 |

## 3. 导航快捷方式

| 快捷键 | 功能描述 |
|--------|----------|
| Ctrl+F | 当前文件查找 |
| Ctrl+R | 当前文件替换 |
| Ctrl+Shift+F | 全局搜索 |
| Ctrl+G | 查找下一个匹配 |
| Ctrl+Shift+G | 查找上一个匹配 |
| Ctrl+B | 跳转到定义 |
| Ctrl+Alt+B | 跳转到实现 |
| Alt+Left/Right | 导航到前一个/后一个编辑位置 |
| Ctrl+Shift+Backspace | 定位到上次编辑位置 |
| Ctrl+Shift+←/→ | 光标跳转到单词开头/结尾 |
| Ctrl+W | 逐步扩大选择范围 |
| Ctrl+Shift+W | 逐步缩小选择范围 |

## 4. 视图快捷方式

| 快捷键 | 功能描述 |
|--------|----------|
| Ctrl+Shift+A | 查找操作 |
| Ctrl+F4 | 关闭当前标签页 |
| Ctrl+W | 关闭当前标签页 |
| Ctrl+E | 显示最近文件列表 |
| Ctrl+Shift+E | 显示最近编辑的文件 |
| F12 | 打开项目面板 |
| Alt+1 | 打开项目面板 |
| Alt+7 | 打开结构视图 |
| Alt+7 | 在 Markdown 中查看文档结构 |
| Ctrl+Alt+H | 调用层次结构 |
| Ctrl+Alt+7 | 查看结构 (仅 Markdown 文件) |

## 5. 代码补全和智能功能

| 快捷键 | 功能描述 |
|--------|----------|
| Ctrl+Space | 基础代码补全 |
| Ctrl+Shift+Space | 智能代码补全 |
| Ctrl+Alt+Space | 快速选择补全 |
| Ctrl+J | 插入活页代码片段 |
| Ctrl+Alt+J | 添加到活页代码片段 |
| Ctrl+Alt+Shift+J | 替换选中文本 |
| Ctrl+Alt+Enter | 创建新行并智能补全 |
| Ctrl+Shift+Enter | 创建新行并智能补全 |

## 6. Markdown 特定功能

### 实时预览
- **Ctrl+Shift+D**: 打开/关闭实时预览面板
- **F12**: 在独立窗口中打开预览
- **Ctrl+Shift+V**: 在预览中粘贴

### 文档导航
- **Alt+F1**: 选择文件在浏览器中打开
- **Ctrl+Shift+Alt+R**: 在浏览器中预览

### 代码折叠
- **Ctrl+Minus (-)**: 折叠选中文本
- **Ctrl+Plus (+)**: 展开选中文本
- **Ctrl+Shift+Minus (-)**: 折叠所有
- **Ctrl+Shift+Plus (+)**: 展开所有

### 目录导航
- **Ctrl+Shift+1**: 展开所有
- **Ctrl+Shift+Minus (-)**: 折叠所有

## 7. 推荐的 Markdown 工作流

### 快速创建文档结构
1. `# Title` + Tab → 一级标题
2. `##` + Tab → 二级标题
3. `-` + Tab → 无序列表项

### 格式化文本
1. 选中文字
2. `Ctrl+B` → 粗体
3. `Ctrl+I` → 斜体

### 创建代码块
1. ``` + Tab
2. 选择编程语言
3. 写入代码

### 快速导航
1. `Ctrl+Shift+A` → 搜索 "Markdown" → 打开预览
2. `Alt+7` → 查看文档结构

## 8. 自定义快捷方式

要自定义快捷方式：

1. 打开 **Settings/Preferences**
2. 导航到 **Keymap**
3. 搜索 "Markdown" 或特定操作
4. 双击操作项修改快捷键
5. 点击 Apply 保存

## 9. 常用插件推荐

- **Markdown Navigator**: 增强的 Markdown 导航
- **Markdown Extended**: 额外的 Markdown 功能
- **Markdown All in One**: 一站式 Markdown 工具集

---

*最后更新: 2024年*
*适用于: IntelliJ IDEA 2021.3+*