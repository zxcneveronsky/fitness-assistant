const API = window.location.protocol + '//' + window.location.host + '/api/v1';

function getToken() {
    return localStorage.getItem('token');
}

function setToken(token) {
    localStorage.setItem('token', token);
}

function clearToken() {
    localStorage.removeItem('token');
}

function isAuth() {
    return !!getToken();
}

function getUserId() {
    try {
        const payload = JSON.parse(atob(getToken().split('.')[1]));
        return payload.userId;
    } catch (e) { return null; }
}

async function deleteAccount() {
    const userId = getUserId();
    if (!userId) return;
    if (!confirm('Вы уверены, что хотите удалить аккаунт? Все данные будут безвозвратно потеряны.')) return;
    if (!confirm('Это действие необратимо. Ваши тренировки, приёмы пищи и прогресс будут удалены. Продолжить?')) return;
    try {
        await request(`/auth/${userId}`, { method: 'DELETE' });
        clearToken();
        window.location.href = '/index.html';
    } catch (e) {
        alert('Ошибка при удалении: ' + e.message);
    }
}

async function request(path, options = {}) {
    const token = getToken();
    const headers = { 'Content-Type': 'application/json', ...options.headers };
    if (token) headers['Authorization'] = `Bearer ${token}`;
    const res = await fetch(`${API}${path}`, { ...options, headers });
    if (res.status === 401 || res.status === 403) {
        clearToken();
        window.location.href = '/login.html';
        return null;
    }
    if (res.status === 204) return null;
    if (!res.ok) {
        const err = await res.json().catch(() => ({ message: res.statusText }));
        throw new Error(err.message || 'Ошибка запроса');
    }
    return res.json();
}

function getActiveSession() {
    const id = localStorage.getItem('activeSessionId');
    const name = localStorage.getItem('activeSessionName');
    return id ? { id: parseInt(id), name } : null;
}

function saveActiveSession(id, name) {
    localStorage.setItem('activeSessionId', id);
    localStorage.setItem('activeSessionName', name);
}

function removeActiveSession() {
    localStorage.removeItem('activeSessionId');
    localStorage.removeItem('activeSessionName');
}

function renderActiveSessionBanner() {
    const banner = document.getElementById('activeSessionBanner');
    if (!banner) return;
    const as = getActiveSession();
    if (!as) return;
    const currentSessionId = new URLSearchParams(window.location.search).get('sessionId');
    const isSessionPage = window.location.pathname.includes('session.html');
    const onThisSession = isSessionPage && currentSessionId && parseInt(currentSessionId) === as.id;
    if (onThisSession) return;

    const nameEl = document.getElementById('bannerName');
    const linkEl = document.getElementById('bannerLink');
    if (nameEl && linkEl) {
        nameEl.textContent = as.name || 'Тренировка';
        linkEl.href = `/session.html?sessionId=${as.id}`;
        banner.classList.remove('hidden');
    } else {
        banner.innerHTML = `<div class="flex items-center justify-between px-10 py-2">
            <span>⏳ <strong>Активная тренировка</strong> — <span>${as.name || 'Тренировка'}</span></span>
            <a href="/session.html?sessionId=${as.id}" class="bg-yellow-200 text-yellow-800 px-4 py-1 rounded-lg font-semibold hover:bg-yellow-300">Продолжить</a>
        </div>`;
        banner.classList.remove('hidden');
    }
}

async function syncActiveSessions() {
    const banner = document.getElementById('activeSessionBanner');
    if (!banner) return;
    try {
        const data = await request('/workout/session/history?page=0&size=10');
        if (data && data.content) {
            const activeSessions = data.content.filter(s => !s.endTime);
            const as = getActiveSession();
            if (activeSessions.length > 0) {
                const latest = activeSessions[0];
                const existingName = localStorage.getItem('activeSessionName');
                const name = existingName || 'Тренировка';
                saveActiveSession(latest.id, name);
                
                if (activeSessions.length > 1) {
                    banner.innerHTML = `<div class="flex items-center justify-between px-10 py-2">
                        <span>⏳ <strong>${activeSessions.length} активные тренировки</strong></span>
                        <div class="flex gap-2">
                            ${activeSessions.slice(0, 3).map(s => `<a href="/session.html?sessionId=${s.id}" class="bg-yellow-200 text-yellow-800 px-3 py-1 rounded-lg text-sm hover:bg-yellow-300">Сессия #${s.id}</a>`).join('')}
                        </div>
                    </div>`;
                    banner.classList.remove('hidden');
                } else {
                    banner.innerHTML = `<div class="flex items-center justify-between px-10 py-2">
                        <span>⏳ <strong>Активная тренировка</strong> — <span id="bannerName">${name}</span></span>
                        <a href="/session.html?sessionId=${latest.id}" class="bg-yellow-200 text-yellow-800 px-4 py-1 rounded-lg font-semibold hover:bg-yellow-300">Продолжить</a>
                    </div>`;
                    banner.classList.remove('hidden');
                }
            } else if (as) {
                removeActiveSession();
                banner.classList.add('hidden');
            }
        }
    } catch (e) {}
}

function formatDate(date) {
    const d = new Date(date);
    return d.toISOString().split('T')[0];
}

function nowISO() {
    return new Date().toISOString();
}

let workoutNameCache = {};

async function getWorkoutNameById(id) {
    if (workoutNameCache[id]) return workoutNameCache[id];
    try {
        const w = await request('/workout/' + id);
        const n = w.name || w.exerciseName || null;
        if (n) workoutNameCache[id] = n;
        return n;
    } catch (e) { return null; }
}

async function syncActiveSessions() {
    const banner = document.getElementById('activeSessionBanner');
    if (!banner) return;
    try {
        const data = await request('/workout/session/history?page=0&size=20');
        if (data && data.content) {
            const active = data.content.filter(function(s) { return !s.endTime; });
            const as = getActiveSession();
            if (active.length > 0) {
                var names = [];
                for (var i = 0; i < Math.min(active.length, 5); i++) {
                    var n = await getWorkoutNameById(active[i].workoutId);
                    names.push({ id: active[i].id, name: n || 'Тренировка' });
                }
                saveActiveSession(active[0].id, names[0].name);

                if (active.length > 1) {
                    var linksHtml = '';
                    for (var j = 0; j < names.length; j++) {
                        linksHtml += '<a href="/session.html?sessionId=' + names[j].id + '" class="bg-yellow-200 text-yellow-800 px-3 py-1 rounded-lg text-sm hover:bg-yellow-300 whitespace-nowrap">' + names[j].name + '</a>';
                    }
                    banner.innerHTML = '<div class="flex items-center justify-between px-10 py-2" style="z-index:50;position:relative">' +
                        '<span>⏳ <strong>' + active.length + ' активные тренировки</strong></span>' +
                        '<div class="flex gap-2 flex-wrap">' + linksHtml + '</div></div>';
                } else {
                    banner.innerHTML = '<div class="flex items-center justify-between px-10 py-2" style="z-index:50;position:relative">' +
                        '<span>⏳ <strong>Активная тренировка</strong> — <span>' + names[0].name + '</span></span>' +
                        '<a href="/session.html?sessionId=' + active[0].id + '" class="bg-yellow-200 text-yellow-800 px-4 py-1 rounded-lg font-semibold hover:bg-yellow-300">Продолжить</a></div>';
                }
                banner.classList.remove('hidden');
            } else if (as) {
                removeActiveSession();
                banner.classList.add('hidden');
            }
        }
    } catch (e) {}
}
