import requests
from bs4 import BeautifulSoup

def fetch_fuel_prices_by_state(state):
    # URLs for petrol and diesel
    urls = {
        "petrol": "https://www.ndtv.com/fuel-prices/petrol-price-in-all-state",
        "diesel": "https://www.ndtv.com/fuel-prices/diesel-price-in-all-state"
    }

    # Initialize prices
    petrol_price = None
    diesel_price = None

    for fuel_type, url in urls.items():
        response = requests.get(url)
        if response.status_code == 200:
            soup = BeautifulSoup(response.content, 'html.parser')
            table = soup.find('table', {'class': 'font-16 color-blue short-nm'})

            if table:
                rows = table.find_all('tr')[1:]  # Skip header row
                for row in rows:
                    cols = row.find_all('td')
                    if len(cols) >= 3:
                        state_name = cols[0].text.strip()
                        price = cols[1].text.strip()

                        # Match state and assign price
                        if state_name.lower() == state.lower():
                            if fuel_type == "petrol":
                                petrol_price = price
                            elif fuel_type == "diesel":
                                diesel_price = price

    return petrol_price, diesel_price
