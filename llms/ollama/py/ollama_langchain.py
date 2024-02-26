#pip install langchain
#pip install langchain-community
#python ollama_langchain.py

from langchain_community.llms import Ollama
from langchain_community.document_loaders import WebBaseLoader
from langchain_community.embeddings import OllamaEmbeddings
from langchain_community.vectorstores import Chroma
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.chains import RetrievalQA
import datetime

current_time = datetime.datetime.now()

print("Start : ", str(current_time.hour) + ":" + str(current_time.minute))

ollama = Ollama(model="llama2")

question = "What is connection timeout agent?"

loader = WebBaseLoader("https://www.albinsblog.com/2023/04/what-is-apache-sling-connection-timeout-agent-and-how-to-use-it-with-aem.html")
data = loader.load()

text_splitter=RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=0)
all_splits = text_splitter.split_documents(data)

oembed = OllamaEmbeddings(model="llama2")
vectorstore = Chroma.from_documents(documents=all_splits, embedding=oembed)

qachain=RetrievalQA.from_chain_type(ollama, retriever=vectorstore.as_retriever(),return_source_documents=True)
result=qachain({"query": question})

print(result["result"])

#print("Source:"+ result["source_documents"][0])

print("End : ", str(current_time.hour) + ":" + str(current_time.minute))

