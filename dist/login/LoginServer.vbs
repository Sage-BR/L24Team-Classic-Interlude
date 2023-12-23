'Get Java path.
Dim path
Set shell = WScript.CreateObject("WScript.Shell")
path = shell.Environment.Item("JAVA_HOME")
If path = "" Then
	MsgBox "Could not find JAVA_HOME environment variable!", vbOKOnly, "Login Server"
Else
	If InStr(path, "\bin") = 0 Then
		path = path + "\bin\"
	Else
		path = path + "\"
	End If
	path = Replace(path, "\\", "\")
	path = Replace(path, "Program Files", "Progra~1")
End If

'Load java.cfg parameters.
Dim parameters
Set file = CreateObject("Scripting.FileSystemObject").OpenTextFile("java.cfg", 1)
parameters = file.ReadLine()
file.Close
Set file = Nothing

'Generate command.
shell.Run "cmd /c start ""L2J 4Team - Login Server"" " & path & "java -cp ../libs/LoginServer.jar; org.l2j.loginserver.LoginServer", 1, False
