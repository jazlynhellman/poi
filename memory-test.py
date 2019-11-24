import subprocess

# this method attempts to measure memory used
# it runs the free command and parses the output
# to see how much memory is being used before and after
# the command runs
def estimate_memory_use(cmd):
    start = subprocess.check_output(["free"]).decode()
    start_used_memory = int(start.split("\n")[1].split("    ")[2])
    subprocess.call(cmd)
    stop = subprocess.check_output(["free"]).decode()
    stop_used_memory = int(stop.split("\n")[1].split("    ")[2])
    return stop_used_memory - start_used_memory

# runs each of the commands specifies below
# change num_runs to change the amount of times each task is called
def run_commands(build_tool, commands):
    memory_use = []
    num_runs = 5
    for cmd in commands:
        print("Running {} {} times".format(cmd, num_runs))
        mem_sum = 0
        for i in range(num_runs):
            subprocess.call(["ant", "clean"])
            if cmd == "site" or cmd == "g-site":
            	subprocess.call(["ant", "init"])

            used_mem = estimate_memory_use([build_tool, cmd])
            mem_sum += used_mem

        memory_use.append(mem_sum / num_runs)
    return memory_use


ant_commands = ["site", "compile", "compile-version", "release-notes", "jenkins", "clean", "check-ooxml-jars", "fetch-ooxml-jars", "init"]
gradle_commands = ["g-site", "g-compile", "g-compile-version", "g-release-notes", "g-jenkins", "g-clean", "g-check-ooxml-jars", "g-fetch-ooxml-jars", "g-init"]

ant_mem_use = run_commands("ant", ant_commands)
gradle_mem_use = run_commands("./gradlew", gradle_commands)
print("Ant memory use:")
print(ant_mem_use)
print()
print("Gradle memory use:")
print(gradle_mem_use)
print()
print("The difference (Ant - Gradle) is:")

diff = []
for i in range(len(ant_mem_use)):
	diff.append(ant_mem_use[i] - gradle_mem_use[i])

print(diff)