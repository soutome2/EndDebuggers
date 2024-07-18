document.addEventListener('DOMContentLoaded', function() {
	const prevButton = document.getElementById('prev-btn');
	const nextButton = document.getElementById('next-btn');
	const tableHead = document.querySelector('.carousel-table thead tr');
	const dateCells = document.querySelectorAll('.carousel-table tbody tr:first-child td');

	// 初期表示時の列数
	const initialColumns = 8;
	let currentColumns = initialColumns;

	// ボタンクリック時の処理
	prevButton.addEventListener('click', function() {
		if (currentColumns > initialColumns) {
			currentColumns -= initialColumns;
			updateTable();
		}
	});

	nextButton.addEventListener('click', function() {
		if (currentColumns < dateCells.length) {
			currentColumns += initialColumns;
			updateTable();
		}
	});

	// テーブルの更新関数
	function updateTable() {
		dateCells.forEach(function(cell, index) {
			if (index === 0) return; // 最初のセルは空のためスキップ
			if (index < currentColumns) {
				cell.classList.add('shown');
				cell.classList.remove('hidden');
				tableHead.children[index].classList.add('shown');
				tableHead.children[index].classList.remove('hidden');
			} else {
				cell.classList.add('hidden');
				cell.classList.remove('shown');
				tableHead.children[index].classList.add('hidden');
				tableHead.children[index].classList.remove('shown');
			}
		});
	}
});