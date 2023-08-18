from fastapi import FastAPI, HTTPException, Request
from pydantic import BaseModel
import subprocess

app = FastAPI()

class ExecuteRequest(BaseModel):
    language: str
    code: str

@app.post('/execute')
def execute(request: ExecuteRequest):
    language = request.language
    code = request.code

    # 选择镜像基于传入的语言
    if language == 'python':
        image = 'python:3.9'
    else:
        raise HTTPException(status_code=400, detail=f"Unsupported language: {language}")

    # 使用Docker执行代码
    cmd = ['docker', 'run', '-i', image, 'python', '-c', code]

    try:
        result = subprocess.check_output(cmd, stderr=subprocess.STDOUT).decode('utf-8').strip()
        return {"result": result}
    except subprocess.CalledProcessError as e:
        raise HTTPException(status_code=400, detail=e.output.decode('utf-8').strip())

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)

