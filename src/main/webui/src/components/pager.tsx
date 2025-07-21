"use client"

import { useState } from "react"
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
    PaginationEllipsis,
} from "@/components/ui/pagination"

interface PagerProps {
    totalPages?: number
    initialPage?: number
    onPageChange?: (page: number) => void
}

export default function Pager({ totalPages = 10, initialPage = 1, onPageChange }: PagerProps) {
    const [currentPage, setCurrentPage] = useState(initialPage)

    const handlePageChange = (page: number) => {
        if (page >= 1 && page <= totalPages) {
            setCurrentPage(page)
            onPageChange?.(page)
        }
    }

    const renderPageNumbers = () => {
        const pages = []
        const showEllipsis = totalPages > 7

        if (!showEllipsis) {
            // Show all pages if 7 or fewer
            for (let i = 1; i <= totalPages; i++) {
                pages.push(
                    <PaginationItem key={i}>
                        <PaginationLink
                            href="#"
                            onClick={(e) => {
                                e.preventDefault()
                                handlePageChange(i)
                            }}
                            isActive={currentPage === i}
                        >
                            {i}
                        </PaginationLink>
                    </PaginationItem>,
                )
            }
        } else {
            // Show ellipsis for more than 7 pages
            if (currentPage <= 4) {
                // Show first 5 pages, ellipsis, last page
                for (let i = 1; i <= 5; i++) {
                    pages.push(
                        <PaginationItem key={i}>
                            <PaginationLink
                                href="#"
                                onClick={(e) => {
                                    e.preventDefault()
                                    handlePageChange(i)
                                }}
                                isActive={currentPage === i}
                            >
                                {i}
                            </PaginationLink>
                        </PaginationItem>,
                    )
                }
                pages.push(
                    <PaginationItem key="ellipsis-end">
                        <PaginationEllipsis />
                    </PaginationItem>,
                )
                pages.push(
                    <PaginationItem key={totalPages}>
                        <PaginationLink
                            href="#"
                            onClick={(e) => {
                                e.preventDefault()
                                handlePageChange(totalPages)
                            }}
                            isActive={currentPage === totalPages}
                        >
                            {totalPages}
                        </PaginationLink>
                    </PaginationItem>,
                )
            } else if (currentPage >= totalPages - 3) {
                // Show first page, ellipsis, last 5 pages
                pages.push(
                    <PaginationItem key={1}>
                        <PaginationLink
                            href="#"
                            onClick={(e) => {
                                e.preventDefault()
                                handlePageChange(1)
                            }}
                            isActive={currentPage === 1}
                        >
                            1
                        </PaginationLink>
                    </PaginationItem>,
                )
                pages.push(
                    <PaginationItem key="ellipsis-start">
                        <PaginationEllipsis />
                    </PaginationItem>,
                )
                for (let i = totalPages - 4; i <= totalPages; i++) {
                    pages.push(
                        <PaginationItem key={i}>
                            <PaginationLink
                                href="#"
                                onClick={(e) => {
                                    e.preventDefault()
                                    handlePageChange(i)
                                }}
                                isActive={currentPage === i}
                            >
                                {i}
                            </PaginationLink>
                        </PaginationItem>,
                    )
                }
            } else {
                // Show first page, ellipsis, current-1, current, current+1, ellipsis, last page
                pages.push(
                    <PaginationItem key={1}>
                        <PaginationLink
                            href="#"
                            onClick={(e) => {
                                e.preventDefault()
                                handlePageChange(1)
                            }}
                            isActive={currentPage === 1}
                        >
                            1
                        </PaginationLink>
                    </PaginationItem>,
                )
                pages.push(
                    <PaginationItem key="ellipsis-start">
                        <PaginationEllipsis />
                    </PaginationItem>,
                )
                for (let i = currentPage - 1; i <= currentPage + 1; i++) {
                    pages.push(
                        <PaginationItem key={i}>
                            <PaginationLink
                                href="#"
                                onClick={(e) => {
                                    e.preventDefault()
                                    handlePageChange(i)
                                }}
                                isActive={currentPage === i}
                            >
                                {i}
                            </PaginationLink>
                        </PaginationItem>,
                    )
                }
                pages.push(
                    <PaginationItem key="ellipsis-end">
                        <PaginationEllipsis />
                    </PaginationItem>,
                )
                pages.push(
                    <PaginationItem key={totalPages}>
                        <PaginationLink
                            href="#"
                            onClick={(e) => {
                                e.preventDefault()
                                handlePageChange(totalPages)
                            }}
                            isActive={currentPage === totalPages}
                        >
                            {totalPages}
                        </PaginationLink>
                    </PaginationItem>,
                )
            }
        }

        return pages
    }

    return (
        <div className="flex justify-center w-full py-8">
            <Pagination>
                <PaginationContent>
                    <PaginationItem>
                        <PaginationPrevious
                            href="#"
                            onClick={(e) => {
                                e.preventDefault()
                                handlePageChange(currentPage - 1)
                            }}
                            className={currentPage === 1 ? "pointer-events-none opacity-50" : ""}
                        />
                    </PaginationItem>

                    {renderPageNumbers()}

                    <PaginationItem>
                        <PaginationNext
                            href="#"
                            onClick={(e) => {
                                e.preventDefault()
                                handlePageChange(currentPage + 1)
                            }}
                            className={currentPage === totalPages ? "pointer-events-none opacity-50" : ""}
                        />
                    </PaginationItem>
                </PaginationContent>
            </Pagination>
        </div>
    )
}
