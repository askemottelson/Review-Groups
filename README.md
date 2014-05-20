Review-Groups
=============

Imagine the following scenario:
You are a teacher, and you have a bunch of students divided into groups. Each group is supposed to critize another group's assignment, and it is your job to distribute the assignments among the groups, so each group get exactly one other group's assignment, and no group is left out. On top of that no group can review the same group as last time!

Solving this manually, you will need to keep a somewhat complicated table of recent assignments and group members, and send each group an email with attachments. This is prone to a lot of mistakes, and worst of all: it is a waste of time.

This project aims to solve this problem! So get a copy of the [latest release](https://github.com/askemottelson/Review-Groups/releases) and follow beneath guides to get started!

Assuming you have `Review.jar` at your disposal and java installed, fire up a terminal and write
```
java -jar Review.jar setup
```
Enter you gmail account settings (currently only supports gmail).

Next add all the groups
```
java -jar Review.jar group add <groupname>
```

Now add the students to each group
```
java -jar Review.jar student add <group_name> <name> <email>
```

Now add the assignments handed in for each group (assignment_no should be 1 if you have only one batch of assignments)
```
java -jar Review.jar assignment add <group_name> <assignment_no> <file_url> 
```

When all groups has a assignment for a specific assignment number, send it away!
```
java -jar Review.jar review send <assignment_no>
```

Customize
---------

Inside etc/email.txt (in the .jar file or in source), you can find the template for the email content that is sent. Edit this to follow whatever you need. You have the following variables available: `$student$` `$group$` `$filename$` `$teacher$`.



Commands
--------
```
setup
group (view | add <groupname> |rm <groupname>)
student (add <group_name> <name> <email> | rm <email>)
assignment (add <group_name> <assignment_no> <file_url> | rm <group_name> <assignment_no>)
review send <assignment_no>
help
```



