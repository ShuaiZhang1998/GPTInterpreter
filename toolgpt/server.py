from flask import Flask, request, jsonify
import subprocess
import json

app = Flask(__name__)

@app.route('/execute', methods=['POST'])
def execute():
    data = request.json
    language = data.get('language')
    code = data.get('code')

    # 选择镜像基于传入的语言
    if language == 'python':
        image = 'python:3.9'
    else:
        return jsonify({"error": f"Unsupported language: {language}"}), 400

    # 使用Docker执行代码
    # bug fix -c需要单独传递
    cmd = ['docker', 'run', '-i', image, 'python', '-c', code]

    try:
        result = subprocess.check_output(cmd, stderr=subprocess.STDOUT).decode('utf-8').strip()
        return jsonify({"result": result})
    except subprocess.CalledProcessError as e:
        return jsonify({"error": e.output.decode('utf-8').strip()}), 400

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')

