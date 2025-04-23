# Bioderma Data Extractor

This project is a simple tool designed to extract and process data from Bioderma product files.

## What It Does

- Parses and extracts structured product information
- Processes raw HTML/XML or other formatted product listings
- Outputs clean, readable data

## How It Works

1. Input files (e.g. scraped pages or product data) are placed in a specified directory.
2. The script processes these files to extract product details like:
   - Name
   - Description
   - Category
   - Ingredients
   - Prices, etc.
3. The results are saved in a structured format (CSV, JSON, etc.)

## Requirements

- Python 3.x
- (Optional) BeautifulSoup, pandas, lxml — depending on your parser setup

Install dependencies (if any):
```bash
pip install -r requirements.txt
```

## License

This project is licensed under a custom proprietary license.  
Use is permitted, but modification, redistribution, and rebranding are prohibited.  
See the [LICENSE.txt](./LICENSE.txt) file for full terms.