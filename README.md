Welcome ECSE 437 Group 7's README for steps to replicate the evaluation of the team's technical improvements on Apache POI.

## Preliminary Steps
Before conducting each experiment, please ensure the following steps have been completed:
1. Clone the repository.
2. `git checkout` the `gradle-build` branch.
Note: Any successful push to this branch will trigger a build.

## Linting Integration
1. Ensure the `checkstyle-8.26-all.jar` and the `google_checks.xml` files are included in your project folder.
2. Copy and paste the following code into the pre-receive hook inside your remote `.git` hooks folder:

```bash
#!/bin/sh
 
checkstyle_cmd=`git config --get checkstyle.cmd`
 
if [ -z "$checkstyle_cmd" ]; then
   echo "Checkstyle command not defined."
   echo "Configure server repository using \"git config --add checkstyle.cmd java -cp ... com.puppycrawl.tools.checkstyle.Main -c ../checkstyle.xml\""
   exit 1
fi
 
REJECT=0
 
while read oldrev newrev refname; do
 
    if [ "$oldrev" == "0000000000000000000000000000000000000000" ];then
        oldrev="${newrev}^"
    fi
    
    files=`git diff --name-only ${oldrev} ${newrev}  | grep -e "\.java$"`
    
    if [ -n "$files" ]; then
        TEMPDIR=`mktemp -d`
        for file in ${files}; do
            mkdir -p "${TEMPDIR}/`dirname ${file}`" &>/dev/null
            git show $newrev:$file > ${TEMPDIR}/${file} 
        done;
    
        files_to_check=`find $TEMPDIR -name '*.java'`
                        
        CHECKS=`${checkstyle_cmd} ${files_to_check} | sed 's/\\\\/\//g' | sed '1d;$d' | sed -e "s#${TEMPDIR}/##g" | sed 's/\(:[0-9]\+\)\+:\?.*//' | sort | uniq -c;exit ${PIPESTATUS[0]}`
        CHECKS_EXIT=$?
        
        if [ ${CHECKS_EXIT} -ne 0 ] ; then
            echo -e "\e[1;31mExecution of checkstyle cmd failed:\e[0m"
            echo -e "\e[1;33m${checkstyle_cmd} [files]\e[0m"
            exit ${CHECKS_EXIT}
        fi
                
        if [ -n "$CHECKS" ]; then 
            echo -e "\e[1;31mCHECKSTYLE ISSUES DETECTED -- REJECTED [$refname]\e[0m"
            echo -e "$CHECKS" | while read num check; do
                 printf '  \e[1;33m%4s\e[0m' $num
                 echo -e "\e[1;33m $check\e[0m"
            done
            REJECT=1
        fi
        rm -rf $TEMPDIR
    fi    
done
 
exit $REJECT
```
   - If attempting to configure this without control of a git server (for example with a GitHub repo) you can simply configure a new bare repository to act as the remote. git init --bare, git remote add local_origin <path_to_bare_repo>, git push local_origin then continue from step 2
3. Configure the hook by running:
```bash
git config --add checkstyle.cmd "java -cp <PATH_TO_CHECKSTYLE.jar> com.puppycrawl.tools.checkstyle.Main -c <PATH_TO_GOOGLE_CHECKS.xml>”
```
4. The hook is easily tested by attempting to push any code inside a .java file that does not conform to Google’s style guidelines. 

## Gradle Migration
1. Set up dependencies by executing `setup.sh`, found in the root of this branch.
2. To run the build speed test, navigate to the root directory and run the command `python3 runtime-tests.py`. 
3. To run the memory usage test, navigate to the root directory and run the command `python3 memory-tests.py`. 

## Frequent Release
1. On a contributing GitHub account, make a push to the `gradle-build` branch on this repository. This push will trigger a Jenkins build.
2. Navigate to Jenkins URL found in the corresponding section in the team's Evaluation Report.
3. Log in to Jenkins with the provided account information, also found in the corresponding section.
4. In Jenkins, first navigate to `poi_build_deploy` to view the build in real-time.
5. Upon successful build, navigate downstream to `poi_build_deploy_output`.
6. Following completion, navigate to Deployed Artifacts URL found in the corresponding section and feel free to download the build artifacts. :) For each build per push, there will be four artifacts with the corresponding date of the push. 
