from enum import Enum
import kotlinInterop
from transformers import pipeline




class Crop(str, Enum):
    MAIZE = "Maize",
    WHEAT = "Wheat",
    SOY_BEANS = "Soy Beans",
    RICE = "Rice",
    BARLEY = "Barley"

class Topic(str, Enum):
    PESTS = "Pests",
    DISEASES = "Diseases",
    PLANNING = "Planning",
    SELLING = "Selling",
    HARVEST = "Harvest",
    STORING = "Storing",
    GROWING = "Growing",
    BUYING_SEEDS = "Buying Seeds"


def getTopicOfMessage(message):
    pipe = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")
    return pipe(message, list(map(str, Topic)))['labels'][0]


def getCropOfMessage(message):
    pipe = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")
    return pipe(message, list(map(str, Crop)))['labels'][0]



kotlinInterop.registerFunction('getTopicOfMessage', getTopicOfMessage)
kotlinInterop.registerFunction('getCropOfMessage', getCropOfMessage)

kotlinInterop.execute()