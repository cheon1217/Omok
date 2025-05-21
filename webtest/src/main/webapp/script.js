document.addEventListener('DOMContentLoaded', function () {
    const boardSize = 15;
    const board = document.getElementById('board');
    const statusMessage = document.getElementById('status-message');
    const turnIndicator = document.getElementById('turn-indicator');
    const restartBtn = document.getElementById('restart-btn');
    const winOverlay = document.querySelector('.win-overlay');
    const winMessage = document.querySelector('.win-message');
    const martialMessage = document.querySelector('.martial-message');
    const closeWinBtn = document.getElementById('close-win');
    const introOverlay = document.querySelector('.intro-overlay');
    const introTitle = document.querySelector('.intro-title');
    const introText = document.querySelector('.intro-text');
    const boardContainer = document.querySelector('.board-container');

    let gameBoard = Array(boardSize).fill().map(() => Array(boardSize).fill(null));
    let currentPlayer = 'black';
    let gameEnded = false;
    let socket = null;

    const userId = window.userId;
    const roomId = window.roomId || new URLSearchParams(location.search).get('room_id');
    if (!roomId) {
        alert("방 코드가 없습니다. 먼저 방을 만들거나 참가해주세요.");
        location.href = "join.jsp";
        return;
    }
    console.log("roomId:", roomId, "userId:", userId);

    socket = new WebSocket(`ws://${location.host}/ws/omok/${roomId}`);

    socket.onopen = () => console.log("WebSocket 연결됨 - Room:", roomId);

    socket.onmessage = (event) => {
        const data = JSON.parse(event.data);
        console.log("📩 수신:", data);

        if (data.type === 'waiting') {
            if (data.count === 1) {
                statusMessage.textContent = "🕐 참가자 기다리는 중...";
            }
        }

        if (data.type === 'playing') {
            introOverlay.style.opacity = '0';
            setTimeout(() => {
                introOverlay.style.display = 'none';
                boardContainer.style.animation = 'board-drop 1.5s forwards';
                setTimeout(() => {
                    board.classList.add('show');
                    createDustEffect();
                }, 100);
            }, 1000);
        }

        const { row, col, stone, gameOver, message } = data;

        if (row !== undefined && col !== undefined && gameBoard[row][col] === null && !gameEnded) {
            const cell = board.querySelector(`[data-row='${row}'][data-col='${col}']`);
            placeStone(cell, stone);
            gameBoard[row][col] = stone;

            if (gameOver) {
                gameEnded = true;
                winMessage.textContent = message;
                martialMessage.textContent = '천하무적 승리의 순간!';
                winOverlay.style.opacity = '1';
                winOverlay.style.pointerEvents = 'auto';
            } else {
                currentPlayer = stone === 'black' ? 'white' : 'black';
                updateGameInfo();
            }
        }
    };

    setTimeout(() => introTitle.style.animation = 'title-appear 1.2s forwards', 500);
    setTimeout(() => {
        introText.style.transition = 'all 1s ease';
        introText.style.opacity = '1';
        introText.style.transform = 'translateY(0)';
    }, 1700);

    function createDustEffect() {
        const container = document.querySelector('.board-container');
        for (let i = 0; i < 30; i++) {
            const dust = document.createElement('div');
            dust.classList.add('dust');
            dust.style.left = `${Math.random() * container.offsetWidth}px`;
            dust.style.bottom = '0';
            dust.style.setProperty('--tx', `${(Math.random() - 0.5) * 200}px`);
            dust.style.setProperty('--ty', `${-Math.random() * 100 - 50}px`);
            dust.style.animation = `dust-spread ${Math.random() * 1 + 0.5}s forwards`;
            container.appendChild(dust);
            setTimeout(() => container.removeChild(dust), 1500);
        }
    }

    function initializeBoard() {
        board.innerHTML = '';
        for (let row = 0; row < boardSize; row++) {
            for (let col = 0; col < boardSize; col++) {
                const cell = document.createElement('div');
                cell.classList.add('cell');
                cell.dataset.row = row;
                cell.dataset.col = col;
                cell.addEventListener('click', handleCellClick);
                board.appendChild(cell);
            }
        }
        updateGameInfo();
    }

    function handleCellClick(event) {
        if (gameEnded) return;

        const cell = event.currentTarget;
        const row = parseInt(cell.dataset.row);
        const col = parseInt(cell.dataset.col);

        fetch('GameServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `row=${row}&col=${col}`
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                socket.send(JSON.stringify({
                    userId: userId,
                    row, col,
                    stone: data.stone,
                    gameOver: data.gameOver,
                    message: data.message
                }));
            } else {
                alert(data.message);
            }
        })
        .catch(err => {
            console.error(err);
            alert('서버 오류 발생');
        });
    }

    function placeStone(cell, player) {
        const stone = document.createElement('div');
        stone.classList.add('stone', player);
        cell.appendChild(stone);
        createEnergyEffect(cell, player);
    }

    function createEnergyEffect(cell, player) {
        const effect = document.createElement('div');
        effect.classList.add('energy-effect');
        effect.style.background = `radial-gradient(circle, ${player === 'black' ? 'rgba(0,0,0,0.6)' : 'rgba(255,255,255,0.6)'} 0%, transparent 70%)`;
        effect.style.width = effect.style.height = '30px';
        cell.appendChild(effect);

        effect.animate([
            { opacity: 0, transform: 'scale(1)' },
            { opacity: 0.7, transform: 'scale(2)' },
            { opacity: 0, transform: 'scale(3)' }
        ], {
            duration: 600,
            easing: 'ease-out'
        });

        const flash = document.createElement('div');
        flash.style.cssText = 'position:fixed;top:0;left:0;width:100%;height:100%;pointer-events:none;z-index:100;animation:flash 0.2s';
        document.body.appendChild(flash);
        setTimeout(() => document.body.removeChild(flash), 200);
        setTimeout(() => cell.removeChild(effect), 600);
    }

    function updateGameInfo() {
        turnIndicator.textContent = `${currentPlayer === 'black' ? '흑돌' : '백돌'} 차례입니다`;
        turnIndicator.className = currentPlayer;
    }

    function restartGame() {
        fetch("GameServlet", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "action=restart"
        });

        gameBoard = Array(boardSize).fill().map(() => Array(boardSize).fill(null));
        currentPlayer = 'black';
        gameEnded = false;
        initializeBoard();
        statusMessage.textContent = '게임을 재시작합니다!';
        winOverlay.style.opacity = '0';
        winOverlay.style.pointerEvents = 'none';
    }

    restartBtn.addEventListener('click', restartGame);
    closeWinBtn.addEventListener('click', () => {
        winOverlay.style.opacity = '0';
        winOverlay.style.pointerEvents = 'none';
    });

    window.addEventListener("beforeunload", () => {
        fetch("GameServlet", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "action=restart"
        });
    });

    initializeBoard();
});


/**
const board = document.getElementById("board");
const status = document.getElementById("status");
const size = 19;

// 판 그리기
for (let row = 0; row < size; row++) {
  for (let col = 0; col < size; col++) {
    const cell = document.createElement("div");
    cell.classList.add("cell");
    cell.dataset.row = row;
    cell.dataset.col = col;
    cell.addEventListener("click", handleClick);
    board.appendChild(cell);
  }
}

function handleClick(e) {
  const row = e.target.dataset.row;
  const col = e.target.dataset.col;

  fetch("GameServlet", {  // ✅ 상대 경로로 요청
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"  // ✅ 폼 방식으로 보냄
    },
    body: `row=${row}&col=${col}`  // ✅ 올바른 전송 포맷
  })
  .then(res => {
    if (!res.ok) throw new Error("서버 오류 또는 404");
    return res.json(); // ✅ JSON으로 받음
  })
  .then(data => {
    if (data.success) {
      e.target.classList.add(data.stone); // black 또는 white
      status.textContent = data.message;

      if (data.gameOver) {
        alert(data.message);
      }
    } else {
      alert(data.message);
    }
  })
  .catch(err => {
    console.error("에러 발생:", err);
    alert("서버 응답에 문제가 있습니다.");
  });
}
**/