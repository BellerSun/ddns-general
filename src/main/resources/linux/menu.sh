#!/usr/bin/env bash

#Shell菜单演示
function menu ()
{
 cat << EOF
----------------------------------------
|***************菜单主页***************|
----------------------------------------
`echo -e "\033[35m 1)查看系统日志\033[0m"`
`echo -e "\033[35m 2)查看系统配置\033[0m"`
`echo -e "\033[35m 3)激活/关闭系统\033[0m"`
`echo -e "\033[35m 4)清理屏幕\033[0m"`
`echo -e "\033[35m 5)退出\033[0m"`
EOF
read -p "请输入对应产品的数字：" num1
case ${num1} in
 1)
  echo "Welcome to 查看系统日志 主页!!"
  query_system_log
  ;;
 2)
  echo "Welcome to 查看系统配置 主页!!"
  query_system_properties
  ;;
 3)
  echo "Welcome to 激活/关闭系统 主页!!"
  manager_active
  ;;
 4)
  clear
  menu
  ;;
 5)
  exit 0
esac
}

function query_system_log ()
{
 cat << EOF
----------------------------------------
|***************查看系统日志****************|
----------------------------------------
`echo -e "\033[35m 输入 数字 选择查看条数\033[0m"`
`echo -e "\033[35m 输入 all 查看全部\033[0m"`
`echo -e "\033[35m 输入 esc 返回主菜单\033[0m"`
EOF
read -p "请输入：" num2
case ${num2} in
 all)
  curl http://localhost:50001/monitor/queryRecord
  query_system_log
  ;;
 esc)
  clear
  menu
  ;;
 *)
  curl http://localhost:50001/monitor/queryRecord/${num2}
  query_system_log
esac
}

function query_system_properties ()
{
 cat << EOF
----------------------------------------
|***************系统参数****************|
----------------------------------------
EOF
curl http://localhost:50001/monitor/querySystemProperties
menu
}

function manager_active ()
{
 cat << EOF
----------------------------------------
|***************激活/关闭 系统****************|
----------------------------------------
`echo -e "\033[35m 输入 true  激活系统\033[0m"`
`echo -e "\033[35m 输入 false 关闭系统\033[0m"`
`echo -e "\033[35m 输入 esc  返回主菜单\033[0m"`
EOF
read -p "请输入：" manager_active_param
case ${manager_active_param} in
 true)
  curl http://localhost:50001/manager/active/true
  manager_active
  ;;
 false)
  curl http://localhost:50001/manager/active/false
  manager_active
  ;;
 esc)
  clear
  menu
  ;;
 *)
  manager_active
esac
}

menu
