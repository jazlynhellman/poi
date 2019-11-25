PWD=`pwd`
sudo apt-get install python3
sudo apt install default-jdk
sudo apt-get install ant
wget -O apache-forrest-0.9-sources.tar.gz http://apache.mirror.globo.tech//forrest/apache-forrest-0.9-sources.tar.gz
tar xzf apache-forrest-0.9-sources.tar.gz
rm apache-forrest-0.9-sources.tar.gz
echo "export FORREST_HOME=\"$PWD/apache-forrest-0.9\"" >> ~/.bashrc 
echo "export PATH=\"$PWD/apache-forrest-0.9/bin:$PATH\"" >> ~/.bashrc
source ~/.bashrc
cd poi
git checkout gradle-build
./gradlew tasks --all | grep g-
