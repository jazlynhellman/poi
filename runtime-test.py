import os
import time

def time_command(cmd):
	start = time.time()
	exit_code = os.system(cmd)
	stop = time.time()
	return stop - start


def run_commands(build_tool, commands):
    run_times = []
    num_runs = 1
    for cmd in commands:
        print("Running {} {} times".format(cmd, num_runs))
        time_sum = 0
        for i in range(num_runs):
            os.system("ant clean")
            if cmd == "site" or cmd == "g-site":
            	os.system("ant init")
            
            exec_time = time_command("{} {}".format(build_tool, cmd))
            time_sum += exec_time

        run_times.append(time_sum / num_runs)
    return run_times


# ant_commands = ["site", "compile", "compile-version", "release-notes", "jenkins", "clean", "check-ooxml-jars", "fetch-ooxml-jars", "init"]
ant_commands = ["compile"]
# gradle_commands = ["g-site", "g-compile", "g-compile-version", "g-release-notes", "g-jenkins", "g-clean", "g-check-ooxml-jars", "g-fetch-ooxml-jars", "g-init"]
gradle_commands = ["g-compile"]

ant_run_times = run_commands("ant", ant_commands)
gradle_run_times = run_commands("./gradlew", gradle_commands)
print("Ant run times:")
print(ant_run_times)
print()
print("Gradle run times:")
print(gradle_run_times)
print()
print("The difference (Ant - Gradle) is:")

diff = []
for i in range(len(ant_run_times)):
	diff.append(ant_run_times[i] - gradle_run_times[i])

print(diff)
