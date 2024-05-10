let currentExpandedTable = null;

function createTableRow(headers, rowData) {
  const row = document.createElement('tr');

  headers.forEach(header => {
    const cell = row.insertCell();

    switch (header) {
      case "URL":
        const link = document.createElement('a');
        link.href = rowData.url;
        link.textContent = rowData.url;
        link.target = '_blank';
        cell.appendChild(link);
        break;
      case "QR Code":
        const qrCodeElement = document.createElement('div');
        qrCodeElement.classList.add('qr-code-cell');
        const qrCodeImage = new QRCode(qrCodeElement, {
          text: rowData.url,
          width: 64,
          height: 64,
          colorDark: "#000000",
          colorLight: "#ffffff",
          correctLevel: QRCode.CorrectLevel.H
        });
        qrCodeElement.dataset.qrCodeText = rowData.url; // Store QR code text data
        cell.appendChild(qrCodeElement);

        // Add hover event listener to zoom QR code
        qrCodeElement.addEventListener('mouseenter', function () {
          qrCodeImage._el.style.transform = 'scale(2)'; // Scale QR code to 200%
        });

        // Reset QR code scale on mouse leave
        qrCodeElement.addEventListener('mouseleave', function () {
          qrCodeImage._el.style.transform = 'scale(1)'; // Reset scale to 100%
        });

        break;
        case "Action":
          // Email Button with Font Awesome Icon
          const emailButton = document.createElement('button');
          emailButton.classList.add('action-cell');
          emailButton.innerHTML = '<i class="fas fa-envelope"></i>'; // Using Font Awesome icon
          emailButton.onclick = function() {
            const emailBody = `Here is the URL: ${rowData.url}`;
            window.open(`mailto:?subject=${rowData.env || rowData.type} URL&body=${encodeURIComponent(emailBody)}`);
          };
          cell.appendChild(emailButton);
  
          // Copy URL Button with Font Awesome Icon
          const copyButton = document.createElement('button');
          copyButton.classList.add('action-cell');
          copyButton.innerHTML = '<i class="fas fa-clipboard"></i>'; // Using Font Awesome icon
          copyButton.onclick = function() {
            navigator.clipboard.writeText(rowData.url)
              .then(() => showTemporaryMessage("URL copied to clipboard!"));
          };
          cell.appendChild(copyButton);
          break;
      default:
        cell.textContent = rowData[header.toLowerCase()] || '';
    }

    cell.style.border = '1px solid black';
  });

  return row;
}


function createCollapsibleTable(tableData, isFirstTable) {
  const urlsContainer = document.getElementById('urls');
  const title = tableData.title;
  const headers = tableData.headers;
  const rows = tableData.urls;

  const collapsibleButton = document.createElement('button');
  collapsibleButton.innerHTML = isFirstTable ? '- ' + title : '+ ' + title;
  collapsibleButton.classList.add('collapsible');

  const tableContainer = document.createElement('div');
  tableContainer.style.display = isFirstTable ? 'block' : 'none';
  tableContainer.classList.add('content');
  urlsContainer.appendChild(collapsibleButton);
  urlsContainer.appendChild(tableContainer);

  collapsibleButton.addEventListener('click', function () {
    const isExpanded = tableContainer.style.display === 'block';
    if (!isExpanded) {
      if (currentExpandedTable) {
        currentExpandedTable.style.display = 'none';
        currentExpandedTable.previousElementSibling.innerHTML = '+ ' + currentExpandedTable.previousElementSibling.textContent.slice(2);
      }
      tableContainer.style.display = 'block';
      this.innerHTML = '- ' + title;
      currentExpandedTable = tableContainer;
    } else {
      tableContainer.style.display = 'none';
      this.innerHTML = '+ ' + title;
      currentExpandedTable = null;
    }
  });

  const exportButton = document.createElement('button');
  exportButton.innerHTML = '<i class="fas fa-file-excel"></i>'
  exportButton.classList.add('export-button');

  // Attach click event to export button
  exportButton.addEventListener('click', function () {
    const table = tableContainer.querySelector('table');
    exportToExcel(table);
  });

  const buttonContainer = document.createElement('div');
  buttonContainer.classList.add('export-table-buttons');
  buttonContainer.appendChild(exportButton);

  tableContainer.appendChild(buttonContainer);

  const table = document.createElement('table');
  table.style.borderCollapse = 'collapse';
  tableContainer.appendChild(table);

  const headerRow = table.createTHead().insertRow();
  headers.forEach(headerText => {
    const headerCell = document.createElement('th');
    headerCell.textContent = headerText;
    headerCell.style.border = '1px solid black';
    headerCell.style.fontWeight = 'bold';
    headerRow.appendChild(headerCell);
  });

  rows.forEach(rowData => {
    table.appendChild(createTableRow(headers, rowData)); // Pass the individual rowData here
  });
}

function loadData() {
  fetch(chrome.runtime.getURL('data.json'))
    .then(response => response.json())
    .then(data => {
      let isFirstTable = true;
      for (const tableName in data) {
        if (data.hasOwnProperty(tableName)) {
          createCollapsibleTable(data[tableName], isFirstTable);
          isFirstTable = false;
        }
      }
    })
    .catch(error => console.error('Error loading JSON data:', error));
}

document.addEventListener('DOMContentLoaded', function () {
  loadData();
});

function exportToExcel(table) {
  const wb = XLSX.utils.table_to_book(table, { sheet: "Sheet1" });
  XLSX.writeFile(wb, "table_data.xlsx");
}

function showTemporaryMessage(message) {
  const messageDiv = document.createElement('div');
  messageDiv.textContent = message;
  messageDiv.classList.add('temporary-message'); // Add CSS class for styling
  document.body.appendChild(messageDiv);

  setTimeout(() => {
    messageDiv.remove();
  }, 3000); // Removes the message after 3000 milliseconds (3 seconds)
}

