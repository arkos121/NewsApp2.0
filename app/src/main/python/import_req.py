import requests
from bs4 import BeautifulSoup
from typing import List

def scrape_delhi_news() -> List[str]:
    """
    Scrapes news content from Times of India that specifically mentions Delhi
    """
    url = "https://timesofindia.indiatimes.com/city/delhi"
    
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    }
    
    try:
        print("Fetching Delhi news...")
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.text, 'html.parser')
        news_items = []

        # Find all news article links
        article_links = soup.find_all('a', href=True)
        
        # Filter for articles containing "Delhi"
        for link in article_links:
            news_text = link.get_text().strip()
            if (
                '/articleshow/' in link['href'] 
                and len(news_text) > 30  # Filter out short links
                and 'Delhi' in news_text  # Only include if "Delhi" is in the text
            ):
                news_items.append(news_text)

        return news_items
    
    except requests.RequestException as e:
        print(f"Error fetching news: {e}")
        return []
    except Exception as e:
        print(f"Unexpected error: {e}")
        return []

if __name__ == "__main__":
    news_items = scrape_delhi_news()
    
    if news_items:
        print("\nLatest News Mentioning Delhi:")
        for idx, news in enumerate(news_items, 1):
            print(f"{idx}. {news}\n")
    else:
        print("No Delhi-specific news items found.")
