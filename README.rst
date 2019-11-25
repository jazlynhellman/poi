Welcome ECSE 437 Group 7's README for steps to replicate the evaluation of the team's technical improvements on Apache POI.

## Preliminary Steps
Before conducting each experiment, please ensure the following steps have been completed:
1. Clone the repository.
2. `git checkout` the `gradle-build` branch.
Note: Any successful push to this branch will trigger a build.

## Linting Integration
1. Ensure the `checkstyle-8.26-all.jar` and the `google_checks.xml` files are included in your project folder.
2. Place the pre-receive hook inside the `.git` hooks folder. 
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
