#pip install llama-index
#pip install llama-index-llms-ollama
#pip install llama-index-embeddings-huggingface
#python ollama_llamaindex.py

from llama_index.core import (
    SimpleDirectoryReader,
    VectorStoreIndex,
    Settings,
)
from llama_index.llms.ollama import Ollama

llm = Ollama(model="llama2", request_timeout=1800)

documents = (
    SimpleDirectoryReader(
        input_dir = './',
        required_exts = [".pdf"])
        .load_data()
)

Settings.llm = llm
Settings.embed_model = "local:BAAI/bge-small-en-v1.5"
Settings.chunk_size=512
Settings.chunk_overlap=64

index = VectorStoreIndex.from_documents(documents)


query_engine = index.as_query_engine()

# Query the index
query="What is AEM?"
response = query_engine.query(query)
print(response)