import os.path
import ssl

try:
    _create_unverified_https_context = ssl._create_unverified_context
except AttributeError:
    pass
else:
    ssl._create_default_https_context = _create_unverified_https_context

from llama_index.legacy import (
    VectorStoreIndex,
    SimpleDirectoryReader,
    StorageContext,
    load_index_from_storage,
    Document
)
import openai
import kotlinInterop



openai.api_key = kotlinInterop.getEnv("OPENAI_API_KEY")

def getAnswer(userMessage, inputDocumentData):
    # load the documents and create the index
    documents = [Document(text=s) for s in inputDocumentData]
    index = VectorStoreIndex.from_documents(documents)

    # query the index
    query_engine = index.as_query_engine(response_mode="tree_summarize")
    response = query_engine.query(userMessage)

    return str(response)




def basicExample():
    # load the documents and create the index
    documents = Document(text="I love Isaac")
    index = VectorStoreIndex.from_documents([documents])

    # query the index
    query_engine = index.as_query_engine(response_mode="tree_summarize")
    response = query_engine.query("Who do I love?")

    return response


kotlinInterop.registerFunction('getAnswer', getAnswer)
kotlinInterop.execute()


