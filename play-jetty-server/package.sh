mvn clean package
mvn dependency:copy-dependencies
rm -rf play-jetty-server/
mkdir play-jetty-server
cp -r target/dependency play-jetty-server/
cp target/play-jetty-server-1.0.jar play-jetty-server/
cp -r src/main/resources/* play-jetty-server/
cp -r bin/* play-jetty-server/
mv play-jetty-server/conf/server-bean.xml play-jetty-server/
chmod +x play-jetty-server/server.sh
tar czf play-jetty-server.tar.gz play-jetty-server
