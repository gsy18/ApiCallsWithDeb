adb install -r $1
pkg=$(~/Android/Sdk/build-tools/26.0.2/aapt dump badging $1|awk -F" " '/package/ {print $2}'|awk -F"'" '/name=/ {print $2}')
act=$(~/Android/Sdk/build-tools/26.0.2/aapt dump badging $1|awk -F" " '/launchable-activity/ {print $2}'|awk -F"'" '/name=/ {print $2}')
echo $pkg
echo $act
echo "----ended----"
adb shell am start -D -n $pkg/$act

DEBUG_PORT=54322
#SOURCE_PATH=app/src/main/java

FILE=/var/tmp/andebug-$(date +%s)
adb jdwp > "$FILE" &
sleep 1
kill -9 $!
JDWP_ID=$(tail -1 "$FILE")
echo "idddd"
echo $JDWP_ID
rm "$FILE"
adb forward tcp:$DEBUG_PORT jdwp:$JDWP_ID
#jdb -sourcepath $SOURCE_PATH -attach localhost:$DEBUG_PORT
