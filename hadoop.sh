#!/usr/bin/env bash
JDK_FOLDER_NAME=jdk
HADOOP_FOLDER_NAME=hadoop

export LANG=zh_CN.UTF-8

#echo 添加hadoop用户
#sudo adduser hadoop
#sudo -u hadoop

echo 从FTP上下载OracleJDK
#curl -O ftp://202.120.222.71/Download_%D7%F7%D2%B5%CF%C2%D4%D8%C7%F8/hadoop/jdk-8u171-linux-x64.tar.gz -u stu-lirui:stu-lirui
#mkdir $JDK_FOLDER_NAME

echo 解压OracleJDK
#tar xf jdk-8u171-linux-x64.tar.gz -C $JDK_FOLDER_NAME

echo 从FTP上下载Hadoop
#curl -O ftp://202.120.222.71/Download_%D7%F7%D2%B5%CF%C2%D4%D8%C7%F8/hadoop/hadoop-2.6.0-cdh5.9.3.tar.gz -u stu-lirui:stu-lirui
#mkdir $HADOOP_FOLDER_NAME

echo 解压Hadoop
#tar xf hadoop-2.6.0-cdh5.9.3.tar.gz -C $HADOOP_FOLDER_NAME

echo "export JAVA_HOME=\$HOME/$JDK_FOLDER_NAME/jdk1.8.0_171" >> ~/.bashrc
echo "export JRE_HOME=\$JAVA_HOME/jre" >> ~/.bashrc
echo "export CLASSPATH=.:\$CLASSPATH:\$JAVA_HOME/lib:\$JRE_HOME/lib" >> ~/.bashrc
echo "export HADOOP_HOME=\$HOME/$HADOOP_FOLDER_NAME/hadoop-2.6.0-cdh5.9.3" >> ~/.bashrc
echo "export HADOOP_INSTALL=\$HADOOP_HOME" >> ~/.bashrc
echo "export HADOOP_MAPRED_HOME=\$HADOOP_HOME" >> ~/.bashrc
echo "export HADOOP_COMMON_HOME=\$HADOOP_HOME" >> ~/.bashrc
echo "export HADOOP_HDFS_HOME=\$HADOOP_HOME" >> ~/.bashrc
echo "export YARN_HOME=\$HADOOP_HOME" >> ~/.bashrc
echo "export HADOOP_COMMON_LIB_NATIVE_DIR=\$HADOOP_HOME/lib/native" >> ~/.bashrc
echo "export PATH=\$PATH:\$JAVA_HOME/bin:\$JRE_HOME/bin:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin" >> ~/.bashrc

source $HOME/.bashrc



