from enum import Enum
import kotlinInterop
from transformers import pipeline




class Crop(str, Enum):
    MAIZE = "MAIZE",
    WHEAT = "WHEAT",
    SOY_BEANS = "SOY_BEANS",
    RICE = "RICE",
    BARLEY = "BARLEY"

class Topic(str, Enum):
    PESTS = "PESTS",
    DISEASES = "DISEASES",
    PLANNING = "PLANNING",
    SELLING = "SELLING",
    HARVEST = "HARVEST",
    STORING = "STORING",
    GROWING = "GROWING",
    BUYING_SEEDS = "BUYING_SEEDS"

def cropResultToEnum(label):
    match label:
        case "MAIZE":
            return Crop.MAIZE
        case "WHEAT":
            return Crop.WHEAT
        case "SOY_BEANS":
            return Crop.SOY_BEANS
        case "RICE":
            return Crop.RICE
        case _:
            return Crop.BARLEY

def topicResultToEnum(label):
    match label:
        case "PESTS":
            return Topic.PESTS
        case "DISEASES":
            return Topic.DISEASES
        case "PLANNING":
            return Topic.PLANNING
        case "SELLING":
            return Topic.SELLING
        case "HARVEST":
            return Topic.HARVEST
        case "STORING":
            return Topic.STORING
        case "GROWING":
            return Topic.GROWING
        case _:
            return Topic.BUYING_SEEDS




def getTopicOfMessage(message):
    pipe = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")
    classifiers = list(map(str, [e.value for e in Topic]))
    result = pipe(message, classifiers)[0]['labels'][0]
    return topicResultToEnum(result)


def getCropOfMessage(message):
    pipe = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")
    classifiers = list(map(str, [e.value for e in Crop]))
    result = pipe(message, classifiers)[0]['labels'][0]
    return cropResultToEnum(result)



kotlinInterop.registerFunction('getTopicOfMessage', getTopicOfMessage)
kotlinInterop.registerFunction('getCropOfMessage', getCropOfMessage)

kotlinInterop.execute()