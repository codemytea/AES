import json
import sys
import traceback


functionMap = {

}

def getEnv(key, args = sys.argv):
    env = {}
    envList = args[4].split("&")
    for item in envList:
        split = item.split("=")
        env[split[0]] = split[1]
    return env[key]



#register a function with the kotlin python interop
def registerFunction(name, function):
    functionMap[name] = function


#reads arguments as JSON from input file
def readArgsFromFile(file):
    with open(file, 'r') as file:
        return json.load(file)


#Write the result of python function execution as JSON to file
def writeResultToFile(filename, result, errorMessage):
    string = json.dumps(result) + "\n" + json.dumps(errorMessage)
    with open(filename, 'w+') as file:
        file.write(string)


#Run an execution of the kotlin python interop
def execute(args = sys.argv):
    uniqueId = args[1]
    function = functionMap[args[2]]
    workingDir = args[3]
    try:
        input = readArgsFromFile(workingDir + "/" + uniqueId + ".args.json")
        functionArgs = input["args"]
        result = function(*functionArgs)
        writeResultToFile(workingDir + "/" + uniqueId + ".result.json", result, None)
    except Exception:
        print(traceback.format_exc())
        writeResultToFile(workingDir + "/" + uniqueId + ".result.json", "", traceback.format_exc())

def getFunctionArgs(args = sys.argv):
    uniqueId = args[1]
    workingDir = args[3]
    input = readArgsFromFile(workingDir + "/" + uniqueId + ".args.json")
    functionArgs = input["args"]
    return functionArgs


