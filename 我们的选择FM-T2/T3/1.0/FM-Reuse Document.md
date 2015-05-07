Reuse Ducument for FM - Team 3
==============================

## Contents

- Requirement
- Design
- Input
- Output
- Usage

## Requirement

1. Receiving the warning message
2. Generating files for each warning messages

## Design

We think that the FM conponent may be used by many other components, so we applied singleton pattern on this class.

We also ensured it thread safe, which makes it easy to use.

When you want to use it, you just need to pass one or two parameters - the message content and the path of the directory for saving log files.

## Input

```java
public void generateWarningMessage(String message);
public void generateWarningMessage(String message, String filepath);
```

| Parameter | Type | Note |
| :------:| :------: | :------: |
| Message | String | Warning message |
| Filepath | String | (Optional) |

## Output

It will generate a text file containing the warning message

Log filename format:

```
<data:yyyy-MM-dd>_<count>.log

e.g.
2015-04-30_0.log
```

Log file format:

```
<date:yyyy-MM-dd> No.<count>
MESSAGE:
<content:Your warning message>

e.g.
2015-04-30 No.0
MESSAGE:
A test warning message

```

## Usage

Firstly, you should inport [FM.jar](https://github.com/TJSoftwareReuse/2012T03/releases/download/v0.1/FM.jar)

```java
immport edu.tongji.FaultManagement;
```

Then, you don't need to use 'new' keyword for initializing. Just do like this:

```java
FaultManagement fm = FaultManagement.getInstance();

fm.generateWarningMessage("[Your message here!]]");
```

## Dependency

[log4j](https://github.com/apache/log4j)

___You may have to download and configure it by yourself___
