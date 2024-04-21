import os.path
from llama_index.legacy import (
    VectorStoreIndex,
    SimpleDirectoryReader,
    StorageContext,
    load_index_from_storage,
    Document
)

import kotlinInterop

def getAnswer(userMessage):
    print(basicExample())

    return "an answer!"


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


