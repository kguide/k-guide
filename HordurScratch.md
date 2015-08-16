#A memo for me

# Introduction #


## ADB Sqlite debugging ##

Cool way to access the sqlite database
<pre>
$ adb [-s <AVD name>] shell<br>
# sqlite3 /data/data/<application package name>/databases/<database name><br>
<br>
Example:<br>
<br>
>adb shell<br>
$ sqlite3 /data/data/com.kroz.app/databases/mydatabase.db<br>
sqlite3 /data/data/com.kroz.app/databases/mydatabase.db<br>
SQLite version 3.5.9<br>
Enter ".help" for instructions<br>
sqlite><br>
</pre>

## Other stuff ##
Ugly "service" icons used in iceland, could be okay as a placeholder icons on our map
![http://new.bergnes.is/img/merki/merki_E.png](http://new.bergnes.is/img/merki/merki_E.png)