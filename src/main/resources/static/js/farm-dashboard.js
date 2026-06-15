const API_URL = '/api/farm/dashboard';
const REFRESH_INTERVAL_MS = 60_000;

function formatDateTime(isoString) {
    if (!isoString) return '-';
    const date = new Date(isoString);
    if (Number.isNaN(date.getTime())) return isoString;
    const pad = (n) => String(n).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} `
        + `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

function formatDateTimeShort(isoString) {
    if (!isoString) return '-';
    const date = new Date(isoString);
    if (Number.isNaN(date.getTime())) return isoString;
    const pad = (n) => String(n).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} `
        + `${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

function escapeHtml(text) {
    if (text == null) return '';
    return String(text)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function rssiWidth(wifiRssi) {
    const width = wifiRssi + 100;
    return Math.min(100, Math.max(0, width));
}

function renderSensorReading(reading) {
    const status = reading.status || 'unknown';
    const errorHtml = reading.errorMessage
        ? `<p class="sensor-error">${escapeHtml(reading.errorMessage)}</p>`
        : '';
    return `
        <div class="sensor-card status-${escapeHtml(status)}">
            <p class="sensor-key">${escapeHtml(reading.sensorKey)}</p>
            <p class="sensor-value">
                <span>${escapeHtml(reading.value)}</span>
                <span>${escapeHtml(reading.unit)}</span>
            </p>
            <p class="sensor-type">${escapeHtml(reading.type)}</p>
            ${errorHtml}
        </div>
    `;
}

function renderActuator(actuator) {
    const stateClass = (actuator.state || '').toLowerCase();
    return `
        <span class="actuator-badge state-${escapeHtml(stateClass)}">
            <span>${escapeHtml(actuator.actuatorKey)}</span>
            (<span>${escapeHtml(actuator.type)}</span>)
            — <strong>${escapeHtml(actuator.state)}</strong>
        </span>
    `;
}

function renderErrors(errors) {
    if (!errors || errors.length === 0) {
        return '<div class="error-panel none"><span>오류 없음</span></div>';
    }
    const items = errors.map((error) => `
        <div class="error-item">
            <strong>${escapeHtml(error.target)}</strong>
            [${escapeHtml(error.code)}]
            <span>${escapeHtml(error.message)}</span>
        </div>
    `).join('');
    return `<div class="error-panel has-errors">${items}</div>`;
}

function renderNetwork(network) {
    if (!network) return '<p class="network-info">네트워크 정보 없음</p>';
    return `
        <div class="network-info">
            <div class="rssi-bar-wrap">
                <span>WiFi RSSI</span>
                <div class="rssi-bar">
                    <div class="rssi-bar-fill" style="width:${rssiWidth(network.wifiRssi)}%"></div>
                </div>
                <span>${escapeHtml(network.wifiRssi)} dBm</span>
            </div>
            <div>IP: <span>${escapeHtml(network.ip)}</span></div>
        </div>
    `;
}

function renderDevice(device) {
    const uptimeMinutes = Math.floor((device.uptimeMs || 0) / 1000 / 60);
    const readings = (device.readings || []).map(renderSensorReading).join('');
    const actuators = (device.actuators || []).map(renderActuator).join('');

    return `
        <article class="device-card">
            <div class="device-card-header">
                <div>
                    <h3 class="device-key">${escapeHtml(device.deviceKey)}</h3>
                    <p class="device-meta">
                        sequence: ${escapeHtml(device.sequence)}
                        · uptime: ${uptimeMinutes}분
                        · json v${escapeHtml(device.jsonVersion)}
                    </p>
                </div>
            </div>
            <h4 class="subsection-title">센서 (readings)</h4>
            <div class="sensor-grid">${readings || '<p>센서 데이터 없음</p>'}</div>
            <h4 class="subsection-title">액추에이터 (actuators)</h4>
            <div class="actuator-list">${actuators || '<span>액추에이터 없음</span>'}</div>
            <h4 class="subsection-title">네트워크 (network)</h4>
            ${renderNetwork(device.network)}
            <h4 class="subsection-title">오류 (errors)</h4>
            ${renderErrors(device.errors)}
        </article>
    `;
}

function renderSensorSlot(slot) {
    return `
        <div class="slot-card">
            <strong>#${escapeHtml(slot.id)}</strong>
            <span>센서 데이터 슬롯</span>
        </div>
    `;
}

function renderDashboard(data) {
    const farm = data.farm;
    document.title = `${farm.name} - 스마트팜 대시보드`;
    document.getElementById('farm-name').textContent = farm.name;
    document.getElementById('farm-meta').textContent =
        `등록: ${formatDateTimeShort(farm.createTime)} · 최종 수정: ${formatDateTimeShort(farm.updateTime)}`;
    document.getElementById('displayed-at').textContent =
        `화면 표시 시각: ${formatDateTime(data.displayedAt)}`;

    const devices = data.devices || [];
    document.getElementById('device-grid').innerHTML = devices.length
        ? devices.map(renderDevice).join('')
        : '<p>등록된 디바이스가 없습니다.</p>';

    const slots = data.sensorSlots || [];
    document.getElementById('slot-grid').innerHTML = slots.length
        ? slots.map(renderSensorSlot).join('')
        : '<p>센서 슬롯이 없습니다.</p>';
}

function showError(message) {
    const el = document.getElementById('load-error');
    el.textContent = message;
    el.hidden = false;
}

function hideError() {
    const el = document.getElementById('load-error');
    el.hidden = true;
    el.textContent = '';
}

async function loadDashboard() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) {
            throw new Error(`서버 응답 오류 (${response.status})`);
        }
        const data = await response.json();
        renderDashboard(data);
        hideError();
    } catch (error) {
        showError(`데이터를 불러오지 못했습니다: ${error.message}`);
    }
}

document.getElementById('refresh-btn').addEventListener('click', loadDashboard);
loadDashboard();
setInterval(loadDashboard, REFRESH_INTERVAL_MS);
