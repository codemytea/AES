import spacy
from enum import Enum

import kotlinInterop


class UserDetails(str, Enum):
    LOCATION_CITY = "LOCATION_CITY"
    LOCATION_COUNTRY = "LOCATION_COUNTRY"
    SMALLHOLDING_SIZE = "SMALLHOLDING_SIZE"
    NAME = "NAME"
    MAIN_CROP = "MAIN_CROP"


# Load the trained spaCy NER model
nlp = spacy.load('model-best')

def processText(text):
    return nlp(text).ents


def getSmallHoldingSizeInfo(text):
    t = processText(text)
    size = [entity.text for entity in t if entity.label_ == "SMALLHOLDING_SIZE"]
    return size[0] if size else None


def getUserName(text):
    t = processText(text)
    name = [entity.text for entity in t if entity.label_ == "NAME"]
    return name[0] if name else None


def getMainCrop(text):
    t = processText(text)
    crop = [entity.text for entity in t if entity.label_ == "MAIN_CROP"]
    return crop[0] if crop else None


def getSmallholdingCity(text):
    t = processText(text)
    city = [entity.text for entity in t if entity.label_ == "LOCATION_CITY"]
    return city[0] if city else None



def getSmallholdingCountry(text):
    t = processText(text)
    country = [entity.text for entity in t if entity.label_ == "LOCATION_COUNTRY"]
    return country[0] if country else None



def getNewInformation(items, text):
    listOfDetails = {item: "" for item in items}
    for item in items:
        match item:
            case UserDetails.LOCATION_COUNTRY:
                listOfDetails[UserDetails.LOCATION_COUNTRY] = getSmallholdingCountry(text)
            case UserDetails.LOCATION_CITY:
                listOfDetails[UserDetails.LOCATION_CITY] = getSmallholdingCity(text)
            case UserDetails.SMALLHOLDING_SIZE:
                listOfDetails[UserDetails.SMALLHOLDING_SIZE] = getSmallHoldingSizeInfo(text)
            case UserDetails.NAME:
                listOfDetails[UserDetails.NAME] = getUserName(text)
            case UserDetails.MAIN_CROP:
                listOfDetails[UserDetails.MAIN_CROP] = getMainCrop(text)

    return listOfDetails


kotlinInterop.registerFunction('getNewInformation', getNewInformation)
kotlinInterop.execute()
