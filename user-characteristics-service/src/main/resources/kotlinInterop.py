import sys
import json
import traceback

functionMap = {

}

def registerFunction(name, function):
    functionMap[name] = function

def readArgsFromFile(file):
    with open(file, 'r') as file:
        return json.load(file)


def writeResultToFile(filename, result, errorMessage):
    string = json.dumps(result) + "\n" + json.dumps(errorMessage)
    with open(filename, 'w+') as file:
        file.write(string)


def execute():
    try:
        args = sys.argv
        uniqueId = args[1]
        function = functionMap[args[2]]
        workingDir = args[3]
        functionArgs = readArgsFromFile(workingDir + "/" + uniqueId + ".args.json")["args"]
        result = function(*functionArgs)
        writeResultToFile(workingDir + "/" + uniqueId + ".result.json", result, None)
    except Exception:
        print(traceback.format_exc())
        writeResultToFile(workingDir+ "/" + uniqueId + ".result.json", "", traceback.format_exc())


