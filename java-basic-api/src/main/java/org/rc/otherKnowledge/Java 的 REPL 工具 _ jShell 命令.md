JShell 是 Java 9 引入的官方 REPL（Read-Evaluate-Print Loop）工具，随 JDK 一同分发，无需额外安装。它以 `/` 开头的指令来管理会话和代码片段（snippet），与直接输入的 Java 代码区分开来。

### 🔍 查看与帮助

这类命令用于了解当前环境中已定义的内容，或获取帮助信息。

**`/help`**
列出所有可用命令和帮助主题。也可以 `/help <命令>` 查看特定命令的详细说明。
```text
jshell> /help
|  Type a Java language expression, statement, or declaration.
|  Or type one of the following commands:
|  /list [<name or id>|-all|-start]    -- list the source you have typed
|  /edit <name or id>                  -- edit a source entry
|  ...
```

**`/vars`、`/methods`、`/types`、`/imports`**
分别显示当前会话中所有已定义的**变量**、**方法**、**类型**和**导入语句**，并附带其类型或签名信息。
```text
jshell> int x = 45
x ==> 45

jshell> String twice(String s) { return s + s; }
|  已创建 方法 twice(String)

jshell> /vars
| int x = 45

jshell> /methods
| String twice(String)
```

**`/list`**
列出已输入的代码片段及其 ID。默认只显示当前活跃的片段。
```text
jshell> int x = 45
jshell> 2 + 2
jshell> /list
1 : int x = 45;
2 : 2 + 2
```
选项 `-all` 会显示所有片段（包括失败、被覆盖、已删除的），`-start` 则显示启动时自动执行的导入语句。
```text
jshell> /list -all
s1 : import java.io.*;
...
1 : int x = 45;
2 : 2 + 2
```

---

### 🛠️ 代码片段的管理

JShell 会为每个有效片段分配一个 ID（如 `1`、`2`），你可以利用这些 ID 或名称来管理它们。

**`/open` 与 `/save`**
`/open <文件>` 用于加载并执行一个 `.jsh` 脚本文件；`/save <文件>` 则将当前会话中的片段保存到文件，便于复用。
```text
jshell> int factorial(int n) {
   ...>     if (n <= 1) return 1;
   ...>     return n * factorial(n - 1);
   ...> }
|  已创建 方法 factorial(int)

jshell> /save factorial.jsh

jshell> /reset
|  重置状态

jshell> /open factorial.jsh
```
执行 `/open` 后，`factorial` 方法重新被定义，可以继续使用。

**`/drop`**
按 ID 或名称删除指定的片段。例如删除之前定义的变量 `x`：
```text
jshell> /vars
| int x = 45

jshell> /drop x
|  已丢弃 变量 x

jshell> /vars
| （无输出，表示没有变量）
```

**`/edit`**
用外部编辑器（默认是 JShell 自带的 Edit Pad）打开指定片段进行修改。修改并保存后，JShell 会用新代码替换原有片段。
```text
jshell> /edit factorial
```
这会打开一个编辑窗口，让你修改 `factorial` 方法的实现。

**`/reset`**
**重置整个会话**，清空所有已输入的片段，恢复到刚启动时的状态。
```text
jshell> /reset
|  重置状态

jshell> /vars
| （无输出）
```

---

### ⚙️ 环境与配置

**`/set`**
调整 JShell 的各类配置，例如设置外部编辑器。以下命令将编辑器设为 `vi`，之后的 `/edit` 就会用 `vi` 打开：
```text
jshell> /set editor "vi"
```

**`/reload`**
重新加载并执行当前会话中的所有有效片段，常用于在修改了某些外部依赖或配置后，重新建立会话状态。

**`/history`**
显示你输入过的所有命令和代码片段的历史记录。

**`/exit`**
退出 JShell 会话。也可以使用快捷键 `Ctrl + D`。
```text
jshell> /exit
|  再见
```

---

### 💡 实用技巧

**Tab 键补全与缩写**
JShell 的命令支持 **Tab 补全**，可以快速列出或补全命令、选项和片段名称。在输入 `/` 后按 Tab，会显示所有可用命令。
```text
jshell> /<Tab>
/!    /?    /drop  /edit  /env  /exit  /help  /history
/imports  /list  /methods  /open  /reload  /reset  /save
/set  /types  /vars
```
只要缩写是**唯一**的，命令和选项都可以简写。例如 `/l -a` 等价于 `/list -all`。

**会话状态的持久性**
JShell 的关键特性是**状态会跨片段保留**。你可以先定义变量，后续片段直接引用它，无需重复声明。
```text
jshell> int base = 10
base ==> 10

jshell> int doubled = base * 2
doubled ==> 20
```

**快速验证代码**
这是 JShell 最直接的价值。例如测试 `java.time` API 或集合操作，无需创建类或 `main` 方法。
```text
jshell> import java.time.*;
jshell> LocalDate.now().plusDays(10)
$5 ==> 202X-XX-XX   // 输出实际日期
```
