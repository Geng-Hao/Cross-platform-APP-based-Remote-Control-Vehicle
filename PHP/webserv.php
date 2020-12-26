<?php
if($_GET["cnct"]==1){ //如果利用 GET 收到變數cnct為 1 則進入迴圈
 $file_name = "command.txt"; //建立一個 txt 檔” command.txt ”
 $file = @file("$file_name"); //讀取檔案
 $open = @fopen("$file_name","w+"); //開啟檔案, 若沒有檔案將建立一份
 @fwrite($open,$_GET["cmd"]); // 將利用 GET 收到的變數 cmd 寫入 txt 檔
 fclose($open); //關閉檔案
 echo @$_GET[cmd]; // 顯示檔案目前內容變數 cmd
}
?>