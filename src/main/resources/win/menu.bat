@echo off &PUSHD %~DP0 &TITLE 运维管理
mode con cols=80 lines=35
color 02
:menu
cls
echo.
echo                        系统菜单   
echo ==========================================================
echo.
echo ――――――――――【1】查看运行日志       ―――――――
echo.
echo ――――――――――【2】查看系统参数       ―――――――
echo.
echo ――――――――――【3】启动服务           ―――――――
echo.
echo ――――――――――【4】关闭服务           ―――――――
echo.
echo ==========================================================
 
set /p user_input=选择并进入命令：
if %user_input%==1 goto a
if %user_input%==2 goto b
if %user_input%==3 goto c
if %user_input%==4 goto d
if not %user_input%=="" goto z 
 
rem 查看日志
:a
echo 打开查看日志页面
explorer "http://localhost:50001/monitor/queryRecord"
echo  查看日志页面打开完成！
echo. & pause
goto menu
 
rem 查看参数
:b
echo.
echo 打开查看系统参数配置页面
explorer "http://localhost:50001/monitor/querySystemProperties"
echo  系统参数配置页面打开完成！
echo. & pause
goto menu
 
rem 启动服务
:c
echo.
echo 打开开启服务页面
explorer "http://localhost:50001/manager/active/true"
echo  查看日志页面打开完成！
echo. & pause
goto menu
 
rem 关闭服务
:d
echo.
echo 打开关闭服务页面
explorer "http://localhost:50001/manager/active/false"
echo  查看日志页面打开完成！
echo. & pause
goto menu

:z
echo.
echo 你的选择无效，请按任意键返回菜单！
echo. & pause
goto menu