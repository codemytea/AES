import geonamescache
import re
import spacy

import kotlinInterop


class UserDetails(str, Enum):
    LOCATION_CITY = "LOCATION_CITY"
    LOCATION_COUNTRY = "LOCATION_COUNTRY"
    SMALLHOLDING_SIZE = "SMALLHOLDING_SIZE"
    NAME = "NAME"
    MAIN_CROP = "MAIN_CROP"


# Get countries and cities
gc = geonamescache.GeonamesCache()
countries = gc.get_countries()
cities = gc.get_cities()

# Load the English NLP model
nlp = spacy.load('en_core_web_sm')


def gen_dict_extract(var, key):
    if isinstance(var, dict):
        for k, v in var.items():
            if k == key:
                yield v
            if isinstance(v, (dict, list)):
                yield from gen_dict_extract(v, key)
    elif isinstance(var, list):
        for d in var:
            yield from gen_dict_extract(d, key)


cities = [x.lower() for x in [*gen_dict_extract(cities, 'name')]]
cities.remove('of')
countries = [x.lower() for x in [*gen_dict_extract(countries, 'name'), 'England', 'Wales', 'Scotland']]


def processText(text):
    return nlp(text).ents


def getSmallHoldingSizeInfo(text):
    t = processText(text)
    size = [entity.text for entity in t if entity.label_ == "QUANTITY"]
    return size[0] if size else None


def getUserName(text):
    t = processText(text)
    name = [entity.text for entity in t if entity.label_ == "PERSON"]
    return name[0] if name else None


def getMainCrop(text):
    t = processText(text)
    crop = [entity.text for entity in t if entity.label_ == "PRODUCT" or entity.label_ == "ORG"]
    return crop[0] if crop else None


def getSmallholdingCity(text):
    t = processText(text)
    city = [entity.text for entity in t if entity.label_ == "GPE"]
    if city:
        return city[0]

    if len(text) != 0:
        for word in re.findall(r'\w+', text.lower()):
            if word in cities:
                return word


def getSmallholdingCountry(text):
    t = processText(text)
    country = [entity.text for entity in t if entity.label_ == "GPE"]
    if country:
        return country[0]

    if len(text) != 0:
        for word in re.findall(r'\w+', text.lower()):
            if word in countries:
                return word


def collect(items, text):
    x = {item: "" for item in items}  # maybe item.value?
    for item in items:
        match item:
            case UserDetails.LOCATION_COUNTRY:
                x[UserDetails.LOCATION_COUNTRY] = getSmallholdingCountry(text)
            case UserDetails.LOCATION_CITY:
                x[UserDetails.LOCATION_CITY] = getSmallholdingCity(text)
            case UserDetails.SMALLHOLDING_SIZE:
                x[UserDetails.SMALLHOLDING_SIZE] = getSmallHoldingSizeInfo(text)
            case UserDetails.NAME:
                x[UserDetails.NAME] = getUserName(text)
            case UserDetails.MAIN_CROP:
                x[UserDetails.MAIN_CROP] = getMainCrop(text)

    return x


kotlinInterop.registerFunction('collect', collect)
kotlinInterop.execute()
