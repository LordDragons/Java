
const canvas = document.getElementById('squareMap');
const ctx = canvas.getContext('2d');

let squareSize = 50;
let rows = 16;
let cols = 24;
let scale = 1;
let heroes = [];
let selectedHero = null;
let editMode = false;
let obstacles = [];



const rowsInput = document.getElementById('rows');
const colsInput = document.getElementById('cols');
const squareSizeInput = document.getElementById('squareSize');
const elementTypeSelect = document.getElementById('elementType');
const applyChangesButton = document.getElementById('applyChanges');
const toggleEditModeButton = document.getElementById('toggleEditMode');

function drawSquare(x, y, size, color = '#d3d3d3') {
    ctx.beginPath();
    ctx.rect(x, y, size, size);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = '#000';
    ctx.stroke();
}

function drawGrid() {
    const scaledSize = squareSize * scale;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
            const x = col * scaledSize;
            const y = row * scaledSize;
            drawSquare(x, y, scaledSize);
        }
    }

    drawObstacles();
    drawHeroes();
    if (selectedHero) {
        drawAccessibleArea(selectedHero);
    }
}

function drawObstacles() {
    const scaledSize = squareSize * scale;
    obstacles.forEach(obstacle => {
        const x = obstacle.col * scaledSize;
        const y = obstacle.row * scaledSize;

        const obstacleColors = {
            water: '#4682B4',
            rock: '#A9A9A9',
            forest: '#228B22',
            sand: '#D2B48C',
            building: '#525252',
            bridge: '#8B4513',
            swamp: '#8c9725'
        };

        const color = obstacleColors[obstacle.type] || '#000';
        drawSquare(x, y, scaledSize, color);
    });
}

// Pour récupérer la liste des héros
async function loadHeroes() {
    try {
        const response = await fetch('/heroes.json');
        if (!response.ok) throw new Error('Erreur lors du chargement des données des héros.');

        const data = await response.json();

        heroes = data.map((heroData, index) => {
            const frenchColor = heroData.color || 'gris';
            const color = colorMap[frenchColor] || 'gray';

            return {
                row: Math.floor(index / cols),
                col: index % cols,
                color: color,
                speed: heroData.speed,
                classe: heroData.classe,
                path: [] // Ajout de la propriété path pour stocker le chemin
            };
        });

        drawGrid(); // Redessiner la carte avec les héros
    } catch (error) {
        console.error('Erreur :', error.message);
    }
}

// Pour dessiner les héros
function drawHeroes() {
    heroes.forEach(hero => {
        const scaledSize = squareSize * scale;
        const x = hero.col * scaledSize + scaledSize / 2;
        const y = hero.row * scaledSize + scaledSize / 2;

        const highlightColor = hero.color;

        hero.path.forEach(path => {
            const pathX = path.col * scaledSize;
            const pathY = path.row * scaledSize;

            // Vérifie si un héros est déjà présent sur la case
            const isOccupied = heroes.some(otherHero => otherHero.row === path.row && otherHero.col === path.col);

            // // Dessine la case en lightblue si elle n'est pas occupée par un autre héros
            // const fillColor = isOccupied ? 'transparent' : 'lightblue';
            // drawSquare(pathX, pathY, scaledSize, fillColor);
        });

        // Dessiner le héros en fonction de sa classe
        ctx.fillStyle = highlightColor;
        ctx.strokeStyle = '#000';
        ctx.lineWidth = 2;

        if (hero.classe === "guerrier") {
            // Dessiner un carré
            ctx.fillRect(x - scaledSize * 0.3, y - scaledSize * 0.3, scaledSize * 0.6, scaledSize * 0.6);
            ctx.strokeRect(x - scaledSize * 0.3, y - scaledSize * 0.3, scaledSize * 0.6, scaledSize * 0.6);
        } else if (hero.classe === "magicien") {
            // Dessiner un cercle
            ctx.beginPath();
            ctx.arc(x, y, scaledSize * 0.3, 0, 2 * Math.PI);
            ctx.fill();
            ctx.stroke();
        } else if (hero.classe === "archer") {
            // Dessiner un triangle
            ctx.beginPath();
            ctx.moveTo(x, y - scaledSize * 0.35);
            ctx.lineTo(x - scaledSize * 0.3, y + scaledSize * 0.3);
            ctx.lineTo(x + scaledSize * 0.3, y + scaledSize * 0.3);
            ctx.closePath();
            ctx.fill();
            ctx.stroke();
        }
    });
}


toggleEditModeButton.addEventListener('click', () => {
    editMode = !editMode;
    toggleEditModeButton.textContent = editMode ? 'Désactiver le mode édition' : 'Activer le mode édition';
});

canvas.addEventListener('click', (event) => {
    const x = event.offsetX;
    const y = event.offsetY;

    const scaledSize = squareSize * scale;
    const col = Math.floor(x / scaledSize);
    const row = Math.floor(y / scaledSize);

    if (editMode) {
        const existingObstacleIndex = obstacles.findIndex(obstacle => obstacle.row === row && obstacle.col === col);

        if (existingObstacleIndex > -1) {
            obstacles.splice(existingObstacleIndex, 1);
        } else {
            const selectedType = elementTypeSelect.value;
            obstacles.push({ row, col, type: selectedType });
        }

        drawGrid();
    }
});

applyChangesButton.addEventListener('click', () => {
    rows = parseInt(rowsInput.value);
    cols = parseInt(colsInput.value);
    squareSize = parseInt(squareSizeInput.value);

    canvas.width = cols * squareSize;
    canvas.height = rows * squareSize;

    heroes = heroes.filter(hero => hero.row < rows && hero.col < cols);
    obstacles = obstacles.filter(obstacle => obstacle.row < rows && obstacle.col < cols);

    drawGrid();
});

function drawAccessibleArea(hero) {
    const scaledSize = squareSize * scale;
    //const radius = hero.speed;

    for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
            if (canMoveTo(hero, row, col)) {
                const x = col * scaledSize;
                const y = row * scaledSize;
                drawSquare(x, y, scaledSize, 'white');
            }
        }
    }
}

const colorMap = {
    "rouge": "red",
    "vert": "green",
    "bleu": "blue",
    "jaune": "yellow",
    "noir": "black",
    "blanc": "white",
    "gris": "gray",
    "orange": "orange",
    "violet": "purple",
    "rose": "pink",
    "marron": "brown",
    "turquoise": "turquoise"
};


canvas.addEventListener('wheel', (event) => {
    event.preventDefault();

    const zoomFactor = 0.1;
    if (event.deltaY < 0) {
        scale += zoomFactor; // Zoom avant
    } else if (event.deltaY > 0) {
        scale = Math.max(0.5, scale - zoomFactor); // Zoom arrière
    }

    drawGrid();
});

function manhattanDistance(row1, col1, row2, col2) {
    return Math.abs(row1 - row2) + Math.abs(col1 - col2);
}

function getNeighbors(row, col) {
    return [
        { row: row - 1, col: col }, // Haut
        { row: row + 1, col: col }, // Bas
        { row: row, col: col - 1 }, // Gauche
        { row: row, col: col + 1 }, // Droite
        { row: row - 1, col: col - 1 }, // Diagonale haut-gauche
        { row: row - 1, col: col + 1 }, // Diagonale haut-droit
        { row: row + 1, col: col - 1 }, // Diagonale bas-gauche
        { row: row + 1, col: col + 1 }  // Diagonale bas-droit
    ];
}

// Fonction de validation du déplacement
function canMoveTo(hero, targetRow, targetCol) {
    const distance = manhattanDistance(hero.row, hero.col, targetRow, targetCol);

    const isOccupied = heroes.some(h => h.row === targetRow && h.col === targetCol);

    const obstacle = obstacles.find(o => o.row === targetRow && o.col === targetCol);
    const isObstacle = obstacle && (
        obstacle.type === 'water' ||
        obstacle.type === 'rock' ||
        obstacle.type === 'forest' ||
        obstacle.type === 'building'
    );

    let movementCost = distance;

    if (obstacle) {
        if (obstacle.type === 'swamp') {
            movementCost += 2; // Marécage = +2 points de mouvement
        }
    }

    return movementCost <= hero.speed && !isOccupied && !isObstacle;
}



// Algorithme A* pour le cheminement
function aStar(hero, targetRow, targetCol) {
    const openList = [];
    const closedList = [];
    const cameFrom = {};

    const gScore = {};
    const fScore = {};

    const start = { row: hero.row, col: hero.col };
    const goal = { row: targetRow, col: targetCol };

    openList.push(start);
    gScore[`${start.row},${start.col}`] = 0;
    fScore[`${start.row},${start.col}`] = manhattanDistance(start.row, start.col, goal.row, goal.col);

    while (openList.length > 0) {
        let current = openList.reduce((lowest, node) => {
            return (fScore[`${node.row},${node.col}`] || Infinity) < (fScore[`${lowest.row},${lowest.col}`] || Infinity)
                ? node : lowest;
        });

        if (current.row === goal.row && current.col === goal.col) {
            let path = [];
            let currentNode = current;
            while (currentNode) {
                path.push(currentNode);
                currentNode = cameFrom[`${currentNode.row},${currentNode.col}`];
            }
            path.reverse();
            return path;
        }

        openList.splice(openList.indexOf(current), 1);
        closedList.push(current);

        const neighbors = getNeighbors(current.row, current.col);

        for (let neighbor of neighbors) {
            if (neighbor.row < 0 || neighbor.row >= rows || neighbor.col < 0 || neighbor.col >= cols) continue;

            const isOccupied = heroes.some(h => h.row === neighbor.row && h.col === neighbor.col);
            const obstacle = obstacles.find(o => o.row === neighbor.row && o.col === neighbor.col);
            const isBlocked = obstacle && (
                obstacle.type === 'water' ||
                obstacle.type === 'rock' ||
                obstacle.type === 'forest' ||
                obstacle.type === 'building'
            );

            if (isOccupied || isBlocked) continue;

            let tempGScore = gScore[`${current.row},${current.col}`] + 1;

            if (obstacle && obstacle.type === 'swamp') {
                tempGScore += 2; // Marécage coûte +2 points
            }

            if (tempGScore > hero.speed) continue;

            if (tempGScore < (gScore[`${neighbor.row},${neighbor.col}`] || Infinity)) {
                cameFrom[`${neighbor.row},${neighbor.col}`] = current;
                gScore[`${neighbor.row},${neighbor.col}`] = tempGScore;
                fScore[`${neighbor.row},${neighbor.col}`] = tempGScore + manhattanDistance(neighbor.row, neighbor.col, goal.row, goal.col);

                if (!openList.some(n => n.row === neighbor.row && n.col === neighbor.col)) {
                    openList.push(neighbor);
                }
            }
        }
    }

    return [];
}


// Fonction pour déplacer le héros
function moveHero(hero, targetRow, targetCol) {
    if (hero.speed > 0) {
        const path = aStar(hero, targetRow, targetCol);
        if (path.length > 0) {
            const distance = manhattanDistance(hero.row, hero.col, targetRow, targetCol);
            hero.path = path; // Met à jour le chemin avec le chemin calculé par A*
            let progress = 0;
            let currentStep = 0;

            function move() {
                if (currentStep < path.length) {
                    const target = path[currentStep];
                    const startX = hero.col * squareSize;
                    const startY = hero.row * squareSize;
                    const endX = target.col * squareSize;
                    const endY = target.row * squareSize;
                    progress += 0.05;

                    const x = startX + (endX - startX) * progress;
                    const y = startY + (endY - startY) * progress;

                    drawGrid();

                    // Dessiner le héros en fonction de sa classe
                    ctx.fillStyle = hero.color;
                    ctx.strokeStyle = '#000';
                    ctx.lineWidth = 2;

                    // Dessiner la forme spécifique en fonction de la classe du héros
                    if (hero.classe === "guerrier") {
                        // Dessiner un carré
                        ctx.fillRect(x + squareSize / 2 - squareSize * 0.3, y + squareSize / 2 - squareSize * 0.3, squareSize * 0.6, squareSize * 0.6);
                        ctx.strokeRect(x + squareSize / 2 - squareSize * 0.3, y + squareSize / 2 - squareSize * 0.3, squareSize * 0.6, squareSize * 0.6);
                    } else if (hero.classe === "magicien") {
                        // Dessiner un cercle
                        ctx.beginPath();
                        ctx.arc(x + squareSize / 2, y + squareSize / 2, squareSize * 0.3, 0, 2 * Math.PI);
                        ctx.fill();
                        ctx.stroke();
                    } else if (hero.classe === "archer") {
                        // Dessiner un triangle
                        ctx.beginPath();
                        ctx.moveTo(x + squareSize / 2, y + squareSize / 2 - squareSize * 0.35);
                        ctx.lineTo(x + squareSize / 2 - squareSize * 0.3, y + squareSize / 2 + squareSize * 0.3);
                        ctx.lineTo(x + squareSize / 2 + squareSize * 0.3, y + squareSize / 2 + squareSize * 0.3);
                        ctx.closePath();
                        ctx.fill();
                        ctx.stroke();
                    }

                    if (progress >= 1) {
                        hero.row = target.row;
                        hero.col = target.col;
                        hero.speed -= distance; // Réduire la vitesse en fonction de la distance parcourue
                        currentStep++;
                        progress = 0;
                    }

                    requestAnimationFrame(move);
                } else {
                    console.log('Déplacement effectué:', hero);
                    drawGrid();
                }
            }
            move();
        } else {
            console.log('Aucun chemin trouvé');
        }
    } else {
        console.log('Le héros ne peut plus se déplacer, vitesse épuisée');
    }
}


canvas.addEventListener('click', (event) => {
    if (editMode) return;

    const x = event.offsetX;
    const y = event.offsetY;

    const scaledSize = squareSize * scale;
    const col = Math.floor(x / scaledSize);
    const row = Math.floor(y / scaledSize);

    const clickedHero = heroes.find(hero => hero.row === row && hero.col === col);

    if (clickedHero) {
        selectedHero = clickedHero;
        console.log('Héros sélectionné:', selectedHero);
        drawGrid();
    } else if (selectedHero) {
        moveHero(selectedHero, row, col);
    }
});

// Fonction pour afficher la légende des obstacles et des héros
function drawLegend() {
    const legendContainer = document.getElementById('legend');
    const obstacleColors = {
        eau: '#4682B4', // water -> eau
        roche: '#A9A9A9', // rock -> roche
        forêt: '#228B22', // forest -> forêt
        sable: '#D2B48C', // sand -> sable
        bâtiment: '#525252', // building -> bâtiment
        pont: '#8B4513', // bridge -> pont
        marais: '#8c9725' // swamp -> marais
    };

    legendContainer.innerHTML = ''; // Réinitialise la légende avant de la remplir

    // Pour chaque type d'obstacle, ajouter une entrée dans la légende
    for (const [type, color] of Object.entries(obstacleColors)) {
        const listItem = document.createElement('li');
        listItem.style.display = 'flex';
        listItem.style.alignItems = 'center';
        listItem.style.marginBottom = '5px';


        const colorBox = document.createElement('div');
        colorBox.style.width = '20px';
        colorBox.style.height = '20px';
        colorBox.style.backgroundColor = color;

        const text = document.createElement('span');
        text.textContent = type.charAt(0).toUpperCase() + type.slice(1); // Capitalise le type (ex : "roche" -> "Roche")

        listItem.appendChild(colorBox);
        listItem.appendChild(text);

        legendContainer.appendChild(listItem);
    }

    // Ajouter les classes des héros à la légende
    const heroClasses = ['guerrier', 'magicien', 'archer'];
    heroClasses.forEach(classe => {
        const heroItem = document.createElement('li');
        heroItem.style.display = 'flex';
        heroItem.style.alignItems = 'center';
        heroItem.style.marginBottom = '5px';
        heroItem.style.width = '20px';

        const text = document.createElement('span');
        text.textContent = `${classe.charAt(0).toUpperCase() + classe.slice(1)} `;

        // Créer un petit canvas pour dessiner l'icône de la classe
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        const size = 20;
        canvas.width = size;
        canvas.height = size;

        if (classe === 'guerrier') {
            // Dessiner un carré pour guerrier
            ctx.fillStyle = '#000';
            ctx.fillRect(0, 0, size, size);
        } else if (classe === 'magicien') {
            // Dessiner un cercle pour magicien
            ctx.fillStyle = '#000';
            ctx.beginPath();
            ctx.arc(size / 2, size / 2, size / 2, 0, 2 * Math.PI);
            ctx.fill();
        } else if (classe === 'archer') {
            // Dessiner un triangle pour archer
            ctx.fillStyle = '#000';
            ctx.beginPath();
            ctx.moveTo(size / 2, 0);
            ctx.lineTo(0, size);
            ctx.lineTo(size, size);
            ctx.closePath();
            ctx.fill();
        }

        // Ajouter l'icône à l'élément de légende
        heroItem.appendChild(canvas);
        text.style.marginLeft = '8px';
        heroItem.appendChild(text);
        legendContainer.appendChild(heroItem);
    });
}

// Appeler cette fonction lors du chargement de la page
drawLegend();

loadHeroes();

function toggleMenu() {
    const menu = document.getElementById('menu');
    menu.classList.toggle('show'); // Toggle la classe 'show' pour afficher ou masquer le menu
}