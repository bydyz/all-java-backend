# CalculateStringNum.java UTF-8 编码损坏分析

## 问题描述

IDE（IntelliJ IDEA）打开文件时弹出警告：

```
The file was loaded in a wrong encoding: 'UTF-8'
```

文件路径：`src/main/java/org/rc/algorithmicProblem/CalculateStringNum.java`

## 分析过程

### 1. 读取文件内容

文件可正常读取，但两处中文注释出现乱码：

```java
// 第11行：找到一个subStr�? 将mainStr设置为找到subStr后面的字符串
// 第15行：改进�?
```

### 2. 检查文件 BOM 头

读取文件前3个字节：

```
70 61 63  →  ASCII: "pac"（即 "package" 开头）
```

**结论**：文件无 BOM（Byte Order Mark），为标准 UTF-8 无 BOM 格式。

### 3. 检测非 ASCII 字节

提取所有大于 `0x7F` 的字节（即多字节 UTF-8 序列）：

```
E6 89 BE  E5 88 B0  E4 B8 80  E4 B8 AA
E5 90 3F  E5 B0 86  E8 AE BE  E7 BD AE
E6 89 BE  E5 88 B0  E5 90 8E  E9 9D A2
E7 9A 84  E5 AD 97  E7 AC A6  E4 B8 B2
E6 94 B9  E8 BF 9B  EF BC 3F
```

### 4. 解码 UTF-8 序列

将有效字节解码为中文字符：

| 字节序列 | 解码结果 | 状态 |
|---------|---------|------|
| `E6 89 BE` | 找 | 正常 |
| `E5 88 B0` | 到 | 正常 |
| `E4 B8 80` | 一 | 正常 |
| `E4 B8 AA` | 个 | 正常 |
| `E5 90 3F` | 后? | **损坏** |
| `E5 B0 86` | 将 | 正常 |
| `E8 AE BE` | 设 | 正常 |
| `E7 BD AE` | 置 | 正常 |
| `E6 94 B9` | 改 | 正常 |
| `E8 BF 9B` | 进 | 正常 |
| `EF BC 3F` | :? | **损坏** |

### 5. 定位损坏字节

UTF-8 中，一个中文字符占 3 个字节，格式为 `E0-EF xx yy`，其中续接字节 `yy` 必须在 `0x80-0xBF` 范围内。

**损坏点 1**（第11行）：

```
E5 90 3F
     └── 0x3F 是 '?'，不在 0x80-0xBF 范围内 → 非法 UTF-8 序列
```

对照 `后` 的正确编码 `E5 90 8E`，可知第三字节 `0x8E` 被替换为 `0x3F`。

**损坏点 2**（第15行）：

```
EF BC 3F
     └── 0x3F 是 '?'，不在 0x80-0xBF 范围内 → 非法 UTF-8 序列
```

对照全角冒号 `：` 的正确编码 `EF BC 9A`，可知第三字节 `0x9A` 被替换为 `0x3F`。

## 损坏原因

最可能的原因：

1. 文件在某次编辑/保存过程中，部分字节被截断或替换
2. 使用了不支持 UTF-8 的工具编辑，导致高位字节被替换为 `?`（`0x3F`）
3. 文件传输过程中（如 git 操作、复制粘贴）发生编码转换错误

## 修复方案

将损坏的两处手动修正为正确的 UTF-8 字符：

| 行号 | 修复前 | 修复后 |
|------|-------|-------|
| 11 | `找到一个subStr�? 将mainStr设置为找到subStr后面的字符串` | `找到一个subStr后，将mainStr设置为找到subStr后面的字符串` |
| 15 | `改进�?` | `改进：` |

修复后验证，损坏的字节模式 `E5 90 3F` 和 `EF BC 3F` 已不存在。

## 预防建议

1. **统一编辑器编码设置**：确保 IDE 和所有编辑器的默认编码为 UTF-8
2. **配置 `.editorconfig`**：
   ```ini
   [*.java]
   charset = utf-8
   ```
3. **IntelliJ IDEA 设置**：`Settings` → `Editor` → `File Encodings` → 全部设为 UTF-8
4. **Git 配置**：避免 git 在 checkout 时自动转换编码
   ```bash
   git config --global core.autocrlf true
   git config --global core.safecrlf warn
   ```
