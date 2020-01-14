-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Jan 14, 2020 at 09:49 PM
-- Server version: 8.0.18
-- PHP Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `de_store`
--

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
CREATE TABLE IF NOT EXISTS `customers` (
  `CUSTOMER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `LOYALTY_CARD` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`CUSTOMER_ID`, `NAME`, `LOYALTY_CARD`) VALUES
(1, 'John Smith', 1),
(2, 'Jane Doe', 0),
(3, 'Jack Donaldson', 0),
(4, 'Brandon Turner', 1),
(5, 'Steven Eddie', 0),
(6, 'Graeme Kelly', 0),
(7, 'Jackie Paxton', 0),
(8, 'Donald Dunn', 0);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `PRODUCT_ID` int(11) NOT NULL,
  `PRODUCT_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `STOCK` int(11) DEFAULT NULL,
  `PRICE` decimal(13,2) DEFAULT NULL,
  PRIMARY KEY (`PRODUCT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`PRODUCT_ID`, `PRODUCT_NAME`, `STOCK`, `PRICE`) VALUES
(1, 'Metal Hammer', 13, '1.00'),
(2, 'White Bread', 16, '2.49'),
(3, 'HP Laptop', 2, '20.50'),
(4, 'Cat Bed', 11, '10.00'),
(5, 'Apple', 55, '50.00'),
(6, 'WiFi Router', 7, '30.00'),
(7, 'Mattress', 11, '200.00'),
(8, 'Reusable Bottle', 145, '8.00'),
(9, 'Tote Bag', 220, '2.49'),
(10, 'Wooden Desk', 9, '83.47'),
(11, 'Jar Candle', 30, '5.00'),
(12, 'Paracetemol', 350, '1.00'),
(13, 'Shampoo', 4, '4.00'),
(14, 'Acoustic Guitar', 20, '300.00');

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
CREATE TABLE IF NOT EXISTS `sales` (
  `SALE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` int(11) NOT NULL,
  `SALE_TYPE` int(11) NOT NULL,
  PRIMARY KEY (`SALE_ID`),
  KEY `PRODUCT_ID` (`PRODUCT_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`SALE_ID`, `PRODUCT_ID`, `SALE_TYPE`) VALUES
(1, 2, 3),
(2, 4, 2),
(3, 6, 2),
(4, 8, 2);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
CREATE TABLE IF NOT EXISTS `transactions` (
  `TRANSACTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` int(11) NOT NULL,
  `CUSTOMER_ID` int(11) NOT NULL,
  `cost` decimal(13,2) DEFAULT NULL,
  `DATE_PURCHASED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`TRANSACTION_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`TRANSACTION_ID`, `PRODUCT_ID`, `CUSTOMER_ID`, `cost`, `DATE_PURCHASED`) VALUES
(1, 3, 1, '20.00', '2019-12-15 01:17:53'),
(2, 4, 1, '20.00', '2019-12-15 01:17:53'),
(3, 1, 1, '15.00', '2019-12-15 01:17:53'),
(4, 4, 2, '10.00', '2019-12-15 01:17:53'),
(5, 2, 1, '2.00', '2019-12-15 01:17:53'),
(6, 4, 1, '10.00', '2019-12-15 01:17:53'),
(7, 1, 2, '150.00', '2019-12-15 01:17:53'),
(8, 4, 2, '10.00', '2019-12-15 16:33:06'),
(9, 2, 1, '2.00', '2019-12-15 16:40:17'),
(10, 1, 1, '150.00', '2019-12-15 16:49:42'),
(11, 1, 1, '150.00', '2019-12-15 16:53:01'),
(12, 1, 2, '1.00', '2019-12-16 01:54:09'),
(13, 1, 1, '0.90', '2019-12-16 01:54:27'),
(14, 2, 1, '2.24', '2019-12-16 01:55:20'),
(15, 7, 1, '189.00', '2019-12-16 02:29:49'),
(16, 8, 1, '7.56', '2019-12-16 02:30:55'),
(17, 9, 1, '0.00', '2019-12-16 02:39:17'),
(18, 10, 1, '75.00', '2019-12-16 02:40:44'),
(19, 6, 1, '32.00', '2019-12-16 02:41:19'),
(20, 10, 1, '77.00', '2019-12-16 02:41:54'),
(21, 10, 1, '78.88', '2019-12-16 02:42:44'),
(22, 10, 1, '78.88', '2019-12-16 02:50:18'),
(23, 2, 5, '2.61', '2019-12-16 13:23:40'),
(24, 14, 3, '315.00', '2019-12-17 12:33:39'),
(25, 1, 4, '1.05', '2019-12-17 12:40:34'),
(26, 6, 4, '31.50', '2019-12-17 12:40:45'),
(27, 8, 4, '8.40', '2019-12-17 12:40:59'),
(28, 9, 3, '2.61', '2019-12-17 14:18:02'),
(29, 14, 3, '315.00', '2019-12-17 14:21:01');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
