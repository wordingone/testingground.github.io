export function greet(name) {
  return `Hello ${name}`;
}

document.getElementById('app').textContent = greet('LumenIO');
