# Cross-platform-APP-based-Remote-Control-Vehicle
## System Overview

![image](https://github.com/Geng-Hao/Cross-platform-APP-based-Remote-Control-Vehicle/blob/master/System%20Overview.png)
本專案開發Android系統的APP以遙控基於樹莓派(Raspberry Pi)的無人載具。跨平台通訊部分利用Android API中的HttpURLConnection類別將操縱無人載具方向之參數夾帶於URL中，利用HTTP傳遞給架於樹莓派上之Web Server(使用php程式)。Web Server於收到參數後將參數存入txt檔，樹莓派中的C語言程式(使用WiringPi函式庫)再利用polling的方式讀取之，並根據參數輸出GPIO電壓，以達成利用APP遙控載具目的。

## Result
![image](https://github.com/Geng-Hao/Cross-platform-APP-based-Remote-Control-Vehicle/blob/master/result.png)
