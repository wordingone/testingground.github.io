import json
from jsonschema import validate
from pathlib import Path

def test_gesture_schema():
  schema_path = Path(__file__).resolve().parent.parent.parent / 'schemas' / 'gesture.json'
  schema = json.loads(schema_path.read_text())
  sample = {'type': 'wave', 'confidence': 0.9}
  validate(instance=sample, schema=schema)
