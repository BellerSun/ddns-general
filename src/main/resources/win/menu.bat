@echo off &PUSHD %~DP0 &TITLE ��ά����
mode con cols=80 lines=35
color 02
:menu
cls
echo.
echo                        ϵͳ�˵�   
echo ==========================================================
echo.
echo �D�D�D�D�D�D�D�D�D�D��1���鿴������־       �D�D�D�D�D�D�D
echo.
echo �D�D�D�D�D�D�D�D�D�D��2���鿴ϵͳ����       �D�D�D�D�D�D�D
echo.
echo �D�D�D�D�D�D�D�D�D�D��3����������           �D�D�D�D�D�D�D
echo.
echo �D�D�D�D�D�D�D�D�D�D��4���رշ���           �D�D�D�D�D�D�D
echo.
echo ==========================================================
 
set /p user_input=ѡ�񲢽������
if %user_input%==1 goto a
if %user_input%==2 goto b
if %user_input%==3 goto c
if %user_input%==4 goto d
if not %user_input%=="" goto z 
 
rem �鿴��־
:a
echo �򿪲鿴��־ҳ��
explorer "http://localhost:50001/monitor/queryRecord"
echo  �鿴��־ҳ�����ɣ�
echo. & pause
goto menu
 
rem �鿴����
:b
echo.
echo �򿪲鿴ϵͳ��������ҳ��
explorer "http://localhost:50001/monitor/querySystemProperties"
echo  ϵͳ��������ҳ�����ɣ�
echo. & pause
goto menu
 
rem ��������
:c
echo.
echo �򿪿�������ҳ��
explorer "http://localhost:50001/manager/active/true"
echo  �鿴��־ҳ�����ɣ�
echo. & pause
goto menu
 
rem �رշ���
:d
echo.
echo �򿪹رշ���ҳ��
explorer "http://localhost:50001/manager/active/false"
echo  �鿴��־ҳ�����ɣ�
echo. & pause
goto menu

:z
echo.
echo ���ѡ����Ч���밴��������ز˵���
echo. & pause
goto menu