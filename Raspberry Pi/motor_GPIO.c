#include <wiringPi.h>
#include <softPwm.h> //引入軟體 pwm 之函式庫 
#include <stdlib.h>
#include <stdio.h>
#define ENA 5 
#define ENB 6 
#define IN1 0 
#define IN2 1 
#define IN3 2 
#define IN4 4

void allMotorOff(); 
void initPin();

int main (void) { 
	
	wiringPiSetup (); 
	initPin(); 	
	allMotorOff();

	while(1){
		
		FILE *file=fopen(“/var/www/html/total_count.txt”,”r”);		
		char control ; //宣告字串 control，代表收到 app 之指令所要求的運動狀態 	
		control=fgetc(file); //自 txt 檔讀取代表運動狀態之字串 control
		
		if(control==’1’){ //若收到字串 1，代表低速前進 
			softPwmWrite(ENA,60); 
			softPwmWrite(ENB,60);
			digitalWrite (IN1,0);
			digitalWrite (IN2,1); 
			digitalWrite (IN3,1); 
			digitalWrite (IN4,0);
		}
 		else if(control==’5’){ //若收到字串 5，高速前進 
			softPwmWrite(ENA,100); 
			softPwmWrite(ENB,100);
			digitalWrite (IN1,0);
			digitalWrite (IN2,1); 
			digitalWrite (IN3,1); 
			digitalWrite (IN4,0);
		}
		else if(control==’2’){ //若收到字串 2，低速後退 
			softPwmWrite(ENA,40);
			softPwmWrite(ENB,40); 
			digitalWrite (IN1,1); 
			digitalWrite (IN2,0); 
			digitalWrite (IN3,0); 
			digitalWrite (IN4,1);
		}
		else if(control==’6’){ //若收到字串 6，高速後退 
			softPwmWrite(ENA,60);
			softPwmWrite(ENB,60); 
			digitalWrite (IN1,1); 
			digitalWrite (IN2,0);
			digitalWrite (IN3,0); 
			digitalWrite (IN4,1);
		}
		else if(control==’3’){ //若收到字串 1，低速左轉 
			softPwmWrite(ENA,60);
			softPwmWrite(ENB,30);
			digitalWrite (IN1,0);
			digitalWrite (IN2,1);
			digitalWrite (IN3,1); 
			digitalWrite (IN4,0);
		}
		else if(control==’7’){ //若收到字串 7，高速左轉 
			softPwmWrite(ENA,100);
			softPwmWrite(ENB,60); 
			digitalWrite(IN1,0);
			digitalWrite (IN2,1);                         
 			digitalWrite (IN3,1);
			digitalWrite (IN4,0);
		}
		else if(control==’4’){ //若收到字串 4，低速右轉 
			softPwmWrite(ENA,30); 
			softPwmWrite(ENB,60);
			digitalWrite (IN1,0);
			digitalWrite (IN2,1); 
			digitalWrite (IN3,1); 
			digitalWrite (IN4,0);
		}
		else if(control==’8’){ //若收到字串 8，高速右轉 
			softPwmWrite(ENA,60); 
			softPwmWrite(ENB,100);
			digitalWrite (IN1,0);
			digitalWrite (IN2,1); 
			digitalWrite (IN3,1); 
			digitalWrite (IN4,0);
		}
			else if(control==’0’){ //若收到字串 0，停止 
			allMotorOff ();
		}
		else if(control==’9’){ //若收到字串 9，緊急停止 
			allMotorOff ();
			break; //緊急停止時跳出迴圈，完全靜止 
		}
	        fclose(file);
	} // end while
 } //主程式結束

 void initPin() { //初始化 pin 腳之函式 
	softPwmCreate(ENA,0,100); 
	softPwmCreate(ENB,0,100); 
	pinMode(IN1, OUTPUT); 
	pinMode(IN2, OUTPUT); 
	pinMode(IN3, OUTPUT);
        pinMode(IN4, OUTPUT); 
 }
 
void allMotorOff() { //所有馬達接停止之函式 
	softPwmWrite(ENA,0); 
	softPwmWrite(ENB,0);
 }