from enum import Enum
from openai import OpenAI
import kotlinInterop


class Stop(str, Enum):
    INFORMATION = "INFORMATION",
    NOTIFICATION = "NOTIFICATION"


#returns List<Pair<String, Stop>>?
def getStopRequests(userMessage):
    print("temp")



kotlinInterop.registerFunction('getStopRequests', getStopRequests)
kotlinInterop.execute()
